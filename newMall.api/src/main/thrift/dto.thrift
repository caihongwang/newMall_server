namespace java com.br.newMall.api.dto

typedef i16 short
typedef i32 int
typedef i64 long

struct Mes {
    1: int id,
    2: int uid,
    3: short type,
    4: string title,
    5: string message,
    6: short status,
    7: long createTime
}

struct MessageDTO {
    1: bool success,
    2: int code,
    3: string message,
    4: list<Mes> mes
}

struct BoolDTO {
    1: bool success,
    2: int code,
    3: string message,
    4: bool value
}

struct ResultDTO{
	1:bool success,
	2:int code,
	3:string message,
	4:int resultListTotal,
	5:list<map<string, string>> resultList,
	6:int allRedPacketMoneyTotal
}

struct ResultMapDTO{
	1:bool success,
	2:int code,
	3:string message,
	4:int resultListTotal,
	5:map<string, string> resultMap
}