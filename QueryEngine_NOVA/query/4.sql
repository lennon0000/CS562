drop view v;
with v as(
    select prod, month, avg(quant) as avg_q
    from sales
    where year = 1997
    group by prod, month)

select sales.prod, sales.month, count(sales.*)
from sales, v prev, v after
where sales.year = 1997
and sales.prod = prev.prod and sales.month = prev.month + 1 and sales.quant > prev.avg_q
and sales.prod = after.prod and sales.month = after.month - 1 and sales.quant < after.avg_q
group by sales.prod, sales.month;