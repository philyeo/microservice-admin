package com.yeopcp.admsvc.admin.domain.util;


import fj.Semigroup;
import fj.data.List;
import fj.data.Option;
import fj.data.Validation;

import lombok.AllArgsConstructor;

import java.text.MessageFormat;

@AllArgsConstructor
public class ErrStack {

    private final List<Exception> list;

    public ErrStack()  {
        list = List.nil();
    }

    public ErrStack(final Exception e) { list = List.single(e); }

    public ErrStack(final String exceptionMessage) { list = List.single(new Exception(exceptionMessage)); }

    public ErrStack(final String pattern, final String... strings) {
        list = List.single(new Exception(MessageFormat.format(pattern, strings)));
    }

    public ErrStack(final Boolean condition, final Exception e) {
        list = condition ? List.nil() : List.single(e);
    }

    public ErrStack(final Boolean condition, final String e) {
        list = condition ? List.nil() : List.single(new Exception(e));
    }

    public ErrStack append(final ErrStack v) { return  new ErrStack(list.append(v.list)); }

    public <T> ErrStack append(final Validation<ErrStack, T > v) {
        return v.isFail() ? new ErrStack((list.append(v.fail().list))) : this;
    }

    public <T> ErrStack append(final Option<Validation<ErrStack, T>> vO) {
        return vO.isNone() ? this : append(vO.some());
    }

    public <T> ErrStack appendList(final List<Validation<ErrStack, T>> list) {
        return list
              .filter(Validation::isFail)
              .map(validation -> validation.fail()).isNotEmpty() ?
              list.filter(Validation::isFail)
                    .map(validation -> validation.fail())
                    .foldLeft1(v1 -> v1::append) : new ErrStack();
    }

    public ErrStack append(final Exception e) { return new ErrStack(list.snoc(e)); }

    public ErrStack append(final String e) { return new ErrStack(list.snoc(new Exception(e))); }

    public ErrStack append(final List<ErrStack> validateds) { return validateds.foldLeft1(v1 -> v1::append); }

    public ErrStack appendOption(final Option<ErrStack> eO) { return eO.isSome() ? this.append(eO.some()): this; }

    public ErrStack appendIfTrue(final Boolean condition, final ErrStack v) {
        return condition ? new ErrStack(list.append(v.list)) : this;
    }

    public ErrStack appendIfTrue(final Boolean condition, final Exception e) {
        return condition ? new ErrStack(list.snoc(e)) : this;
    }

    public ErrStack appendIfTrue(final Boolean condition, final String e) {
        return condition ? new ErrStack(list.snoc(new Exception(e))) : this;
    }

    public Boolean isSuccess() { return list.isEmpty(); }

    public Boolean isFail() { return  !list.isEmpty(); }

    public String toString() {
        //Todo: check whether it needs to be concatenated
        return list.toString();
    }

    public  List<String> getExceptionMessages() { return list.map(Throwable::getMessage); }

    public static ErrStack of(final String string) {return new ErrStack(string); }

    public static ErrStack of(final String pattern, final String... strings) { return new ErrStack(pattern, strings); }

    public <T> Validation<ErrStack, T> V() {return Validation.fail(this); }

    public static final Semigroup<ErrStack> semigroup = Semigroup.semigroup(es1 -> es1::append);

}
