alter table `offer_image` Add column thumbtype int not null default 0;
alter table `offer` Add column thumblistimgurl VARCHAR(256);
alter table `offer` Add column thumbshowimgurl VARCHAR(256);
alter table `label` Add column parentcategoryid int not null default 0;
alter table `label` Add column currentcategoryid int not null default 0;
alter table `label` Add column levelId int not null default 0;
alter table `shop_label` Add column parentcategoryid int not null default 0;
alter table `shop_label` Add column currentcategoryid int not null default 0;


 
