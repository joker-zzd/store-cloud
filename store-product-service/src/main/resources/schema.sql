CREATE TABLE IF NOT EXISTS pms_product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_id BIGINT,
    category_id BIGINT,
    name VARCHAR(255),
    pic VARCHAR(255),
    product_sn VARCHAR(255),
    publish_status INT,
    new_status INT,
    recommend_status INT,
    sort INT,
    price DECIMAL(10, 2),
    description VARCHAR(1000),
    create_time TIMESTAMP,
    update_time TIMESTAMP
);
