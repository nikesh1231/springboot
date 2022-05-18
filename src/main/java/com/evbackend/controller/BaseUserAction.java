package com.evbackend.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BaseUserAction {

    List children;
    Integer id;
    Integer pid;
    String dataLang;

}
