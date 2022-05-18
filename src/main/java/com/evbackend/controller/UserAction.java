package com.evbackend.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAction{
    //osa.pid,osa.id,osa.nam
    Integer pid;
    Integer id;
    String name;
    String dataLang;


}