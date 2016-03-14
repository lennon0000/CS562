/*
select cust, month, 1_sum_quant, 2_sum_quant, 3_sum_quant
from sales
where year = 1997
group by cust, month; x, y, z
such that x.cust = cust,
          y.cust = cust and y.month < month,
          z.cust = cust and z.month = month
having    sum(y.quant) < sum(x.quant) / 2 and
          sum(y.quant) + sum(z.quant) >= sum(x.quant) / 2
*/

with v1 as(
    select cust, sum(quant) as sum_year
    from sales
    where sales.year = 1997
    group by cust),

v2 as(
    select cust, month, sum(quant) as sum_month
    from sales
    where sales.year = 1997
    group by cust, month),

v3 as(
    select v2.cust, v2.month, sum(sales.quant) as sum_cum
    from sales, v2
    where sales.year = 1997 and sales.cust = v2.cust and sales.month < v2.month
    group by v2.cust, v2.month)

select v2.cust, v2.month, v1.sum_year, v2.sum_month, v3.sum_cum
from v1, v2, v3
where v1.cust = v2.cust and v1.cust = v3.cust and v2.month = v3.month
and v3.sum_cum < v1.sum_year * 1.0 / 2 and v3.sum_cum + v2.sum_month >= v1.sum_year * 1.0 / 2;