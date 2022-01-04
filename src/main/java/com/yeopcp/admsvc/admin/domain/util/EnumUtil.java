package com.yeopcp.admsvc.admin.domain.util;


import com.yeopcp.admsvc.admin.domain.constant.LabelEnum;
import fj.Try;
import fj.data.List;
import fj.data.Option;
import fj.data.Validation;

import javax.validation.Valid;

public class EnumUtil {

    public static <T extends Enum<T>> Validation<ErrStack, List<T>> toEnumListV(final String string, final Class<T> enumType) {
        if(string == null) {
            return Validation.success(List.nil());
        }

        List<Validation<ErrStack, T>> list = List.arrayList(string.split(","))
              .filter(s -> s.trim().length() > 0)
              .map(s -> toEnumV(s, enumType));

        final List validateds = list.filter(Validation::isFail).map(v-> v.fail());
        if(validateds.isEmpty()) {
            return Validation.success(list.map(v -> (T) v.success()));
        } else {
            return Validation.fail(new ErrStack().append(validateds));
        }
    }

    public static <T extends Enum<T>> Validation<ErrStack, List<T>> toEnumListV(final  List<String> strings, final Class<T> enumType) {
        if(strings == null) {
            return Validation.success(List.nil());
        }

        List<Validation<ErrStack, T>> list = strings
              .filter(s -> s.trim().length() > 0)
              .map(s -> toEnumV(s, enumType));

        final List validateds = list.filter(Validation::isFail).map(v -> v.fail());
        if(validateds.isEmpty()) {
            return Validation.success(list.map(v -> (T) v.success()));
        } else {
            return Validation.fail(new ErrStack().append(validateds));
        }
    }

    public static <T extends Enum<T>> Validation<ErrStack, List<T>> toEnumListByLabel(final  String string, final Class<T> enumType) {
        if(string == null) {
            return Validation.success(List.nil());
        }

        List<Validation<ErrStack, T>> list = List.arrayList(string.split(","))
              .filter(s -> s.trim().length() > 0)
              .map(s -> toEnumByLabelV(s, enumType));

        final List validateds = list.filter(Validation::isFail).map(v -> v.fail());
        if(validateds.isEmpty()) {
            return Validation.success(list.map(v -> (T) v.success()));
        } else {
            return Validation.fail(new ErrStack().append(validateds));
        }
    }

    public static <T extends Enum<T>> Validation<ErrStack, T> toEnumV(final String s, final Class<T> enumType) {
        return Try.f(() -> Enum.valueOf(enumType, s)).f().f().map(ErrStack::new);
    }

    public static <T extends Enum<T>> Validation<ErrStack, T> toEnumV(final String s, final T defEnum, final Class<T> enumType) {
        if (s == null || s.trim().length() == 0) {
            return Validation.success(defEnum);
        }
        return Try.f(() -> Enum.valueOf(enumType, s)).f().f().map(ErrStack::new);
    }

    public static <T extends Enum<T>> Validation<ErrStack, T> toEnumByLabelV(final String s, final Class<T> enumType) {
        return Try.f(() -> Enum.valueOf(enumType, trimLabelToEnumName(s))).f().f().map(ErrStack::new);
    }

    public static <T extends Enum<T>> Option<T> toEnumO(final String s, final Class<T> enumType) {
        return toEnumV(s, enumType).toOption();
    }

    // String together all the enum names separated by ","
    public static <T extends Enum<T>> String toString(final List<T> list) {
        return list.foldLeft(s -> t -> s.concat(",").concat(t.name()), "");
    }

    public static <T extends LabelEnum> String toLabelString(final List<T> list) {
        return list.foldLeft(s -> t -> s.concat(",").concat(t.getLabel()), "");
    }

    private static String trimLabelToEnumName(String label) {
        return label.substring(label.indexOf("-") + 1)
              .trim()
              .replaceAll(" - ", "_")
              .replaceAll("-", "_")
              .replaceAll("\\?", "")
              .replaceAll(" / ", "_")
              .replaceAll("/ ", "_")
              .replaceAll("/", "_")
              .replaceAll(" & ", "_")
              .replaceAll("\\.", "")
              .replaceAll(":", "")
              .replaceAll("[()]", "")
              .replaceAll("\\s", "_")
              .replaceAll(", ", "_")
              .replaceAll("&", "_")
              .toUpperCase();
    }
}



