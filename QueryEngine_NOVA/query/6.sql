
with v1 as(
    select prod, count(*) as cnt_1
    from sales
    group by prod),

v2 as(
    select prod, quant
    from sales
    group by prod, quant),

v3 as(
    select v2.prod, v2.quant, count(sales.*) as cnt_2
    from sales, v2
    where sales.prod = v2.prod and sales.quant < v2.quant
    group by v2.prod, v2.quant)

select v1.prod, v3.quant
from v1, v3
where v1.prod = v3.prod and v3.cnt_2 = v1.cnt_1 / 2;
    