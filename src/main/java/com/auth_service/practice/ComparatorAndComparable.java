package com.auth_service.practice;

import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComparatorAndComparable {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        students.add(new Student("Ravi", 22));
        students.add(new Student("Aman", 20));
        students.add(new Student("Karan", 25));
        Collections.sort(students);
        System.out.println("Sorted by name: " + students);
    }
}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
class Student implements Comparable<Student> {
    private String name;
    private Integer age;

    @Override
    public int compareTo(Student o) {
        return this.name.compareTo(o.name);
    }
}

