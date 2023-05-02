    RINT
    CMP #17, R1
    BGT else
    WINT
    WSTR " <= 17"
    BRA fi

else:
    WINT
    WSTR " > 17"

fi:
    WNL
    