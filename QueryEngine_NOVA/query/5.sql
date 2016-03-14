
with v1 as(
    select cust, prod, avg(quant) as avg_1
    from sales
    group by cust, prod),

v2 as(
    select v1.cust, v1.prod, avg(sales.quant) as avg_2
    from sales, v1
    where sales.cust <> v1.cust and sales.prod = v1.prod
    group by v1.cust, v1.prod)

select v1.cust, v1.prod, v1.avg_1, v2.avg_2
from v1, v2
where v1.cust = v2.cust and v1.prod = v2.prod;