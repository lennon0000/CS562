with v1 as (
    select prod, month, sum(quant) as sum_1
    from sales
    where year = 1997
    group by prod, month),

v2 as (
    select prod, sum(quant) as sum_2
    from sales
    where year = 1997
    group by prod)

select v1.prod, v1.month, v1.sum_1 * 1.0 / v2.sum_2
from v1, v2
where v1.prod = v2.prod;