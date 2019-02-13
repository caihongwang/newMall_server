CREATE PROCEDURE sp_jsonarray_extract()
  BEGIN
    DECLARE v_i INT DEFAULT 1;
    DROP TABLE IF EXISTS info_incre_table;
    CREATE TABLE info_incre_table (
      ID INT NOT NULL,
      PRIMARY KEY (ID)
    )
      ENGINE = MyISAM
      DEFAULT CHARSET = utf8; -- 自增临时表
    WHILE (v_i < 100) DO -- 100代表解json数组的最大长度
      INSERT INTO info_incre_table VALUES (v_i);
      SET v_i = v_i + 1;
    END WHILE;
    SELECT
      a.title,
      substring_index(substring_index(replace(replace(replace(replace(
                                                                  JSON_EXTRACT(
                                                                      a.info,
                                                                      '$[*].Symbol'),
                                                                  ' ', ''), '"',
                                                              ''), '[', ''),
                                              ']', ''), ',', b.ID), ',',
                      -1) AS symbol
    FROM jsonarray_test a
      JOIN info_incre_table b ON b.ID <= (
        length(JSON_EXTRACT(a.info, '$[*].Symbol')) -
        length(replace(JSON_EXTRACT(a.info, '$[*].Symbol'), ',', '')) + 1)
    ORDER BY a.title;
  END;
