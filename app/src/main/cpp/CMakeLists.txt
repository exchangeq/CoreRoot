cmake_minimum_required(VERSION 3.22.1)
project(coreroot C)

# CoreRoot 原生桥接库 —— 原创实现，不依赖任何已知厂商二进制
add_library(coreroot SHARED
    coreroot.c
)

find_library(log-lib log)

target_link_libraries(coreroot
    ${log-lib}
)

target_compile_options(coreroot PRIVATE
    -Wall
    -Wextra
    -Wno-unused-parameter
)
