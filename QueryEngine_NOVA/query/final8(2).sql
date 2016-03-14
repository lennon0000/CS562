/*
select prod, month, 2_count_prod, 3_count_prod
from sales
where year = 1997
group by prod, month; x, y, z
such that x.prod = prod and x.month = month,
          y.prod = prod and y.month = month - 1 and y.quant > avg(x.quant)
          z.prod = prod and z.month = month + 1 and z.quant > avg(x.quant)
having count(y.*) = count(z.*)
*/
with v1 as(
    select prod, month, avg(quant) as avg_q
    from sales
    where year = 1997
    group by prod, month),

v2 as(
    select v1.prod, v1.month, count(sales.*) as cnt_prev
    from sales, v1
    where sales.year = 1997 and sales.prod = v1.prod and sales.month = v1.month - 1
    and sales.quant > v1.avg_q
    group by v1.prod, v1.month),

v3 as(
    select v1.prod, v1.month, count(sales.*) as cnt_after
    from sales, v1
    where sales.year = 1997 and sales.prod = v1.prod and sales.month = v1.month + 1
    and sales.quant > v1.avg_q
    group by v1.prod, v1.month)

select v2.prod, v2.month, v2.cnt_prev, v3.cnt_after
from v2, v3
where v2.prod = v3.prod and v2.month = v3.month and v2.cnt_prev = v3.cnt_after;