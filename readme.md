# Readme of Turing machine simulator
> Copyright 2013, Kun Wang (nju.wangkun@gmail.com).

> Department of Computer Science & Technology, Nanjing University, China.

## Introduction
This is a Turing machine simulator based on a nice Java Turing machine simulator
from http://introcs.cs.princeton.edu/java/74turing/.

The syntax of Turing machine is totally from the book of
"*Introduction to Models of Computation*" by professor Fangmin Song, which you can buy from [Introduction to Models of Computation](http://www.amazon.cn/%E8%AE%A1%E7%AE%97%E6%9C%BA%E7%A7%91%E5%AD%A6%E4%B8%8E%E6%8A%80%E6%9C%AF%E7%A0%94%E7%A9%B6%E7%94%9F%E7%B3%BB%E5%88%97%E6%95%99%E6%9D%90-%E8%AE%A1%E7%AE%97%E6%A8%A1%E5%9E%8B%E5%AF%BC%E5%BC%95-%E5%AE%8B%E6%96%B9%E6%95%8F/dp/B008N6TA4U/ref=sr_1_1?ie=UTF8&qid=1421406550&sr=8-1&keywords=%E8%AE%A1%E7%AE%97%E6%A8%A1%E5%9E%8B%E5%AF%BC%E5%BC%95).

## Description

`bin`
> Contains the compiled .class files. The "images" sub-directory is very important as it stores the required images for the simulator. In the next update, I will try to move it out of the "bin" directory.

`files`
> Contains the program description for specific operations. > Examples include:
> + **Adder.tur**: a program for adding two non-negative integers.
> + **copyString.tur**: a program for copy a string of *11...11*s.
>
> In these two files, I have give a detail description of the requirements for writing a program that can be accepted by the simulator.

`src`
> Source files of the simulator.

**Attention!** All of the above three directories are required for running the simulator.
## How to Use

*Step1.* Download the repository.
*Step2.* Import into eclipse.
*Step3.* Run it!
