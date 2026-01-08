-- liquibase formatted sql

-- changeset klillenb:002-seed-sectors

INSERT INTO sectors (name)
SELECT v.name
FROM (VALUES ('Manufacturing'),
             ('Other'),
             ('Service')) v(name)
WHERE NOT EXISTS (SELECT 1
                  FROM sectors s
                  WHERE s.name = v.name);

INSERT INTO sectors (name, parent_id)
SELECT v.name, p.id
FROM (VALUES ('Construction materials', 'Manufacturing'),
             ('Electronics and Optics', 'Manufacturing'),
             ('Food and Beverage', 'Manufacturing'),
             ('Furniture', 'Manufacturing'),
             ('Machinery', 'Manufacturing'),
             ('Metalworking', 'Manufacturing'),
             ('Plastic and Rubber', 'Manufacturing'),
             ('Printing', 'Manufacturing'),
             ('Textile and Clothing', 'Manufacturing'),
             ('Wood', 'Manufacturing'),
             ('Creative industries', 'Other'),
             ('Energy technology', 'Other'),
             ('Environment', 'Other'),
             ('Business service', 'Service'),
             ('Engineering', 'Service'),
             ('Information Technology and Telecommunications', 'Service'),
             ('Tourism', 'Service'),
             ('Translation services', 'Service'),
             ('Transport and Logistics', 'Service')) v(name, parent_name)
         JOIN sectors p ON p.name = v.parent_name
WHERE NOT EXISTS (SELECT 1
                  FROM sectors s
                  WHERE s.name = v.name);

INSERT INTO sectors (name, parent_id)
SELECT v.name, p.id
FROM (VALUES ('Bakery & confectionery products', 'Food and Beverage'),
             ('Beverages', 'Food and Beverage'),
             ('Fish & fish products', 'Food and Beverage'),
             ('Meat & meat products', 'Food and Beverage'),
             ('Milk & dairy products', 'Food and Beverage'),
             ('Other', 'Food and Beverage'),
             ('Sweets & snack food', 'Food and Beverage'),

             ('Bathroom/sauna', 'Furniture'),
             ('Bedroom', 'Furniture'),
             ('Children''s room', 'Furniture'),
             ('Kitchen', 'Furniture'),
             ('Living room', 'Furniture'),
             ('Office', 'Furniture'),
             ('Other (Furniture)', 'Furniture'),
             ('Outdoor', 'Furniture'),
             ('Project furniture', 'Furniture'),

             ('Machinery components', 'Machinery'),
             ('Machinery equipment/tools', 'Machinery'),
             ('Manufacture of machinery', 'Machinery'),
             ('Maritime', 'Machinery'),
             ('Metal structures', 'Machinery'),
             ('Other', 'Machinery'),
             ('Repair and maintenance service', 'Machinery'),

             ('Construction of metal structures', 'Metalworking'),
             ('Houses and buildings', 'Metalworking'),
             ('Metal products', 'Metalworking'),
             ('Metal works', 'Metalworking'),

             ('Packaging', 'Plastic and Rubber'),
             ('Plastic goods', 'Plastic and Rubber'),
             ('Plastic processing technology', 'Plastic and Rubber'),
             ('Plastic profiles', 'Plastic and Rubber'),

             ('Advertising', 'Printing'),
             ('Book/Periodicals printing', 'Printing'),
             ('Labelling and packaging printing', 'Printing'),

             ('Clothing', 'Textile and Clothing'),
             ('Textile', 'Textile and Clothing'),

             ('Other (Wood)', 'Wood'),
             ('Wooden building materials', 'Wood'),
             ('Wooden houses', 'Wood'),

             ('Data processing, Web portals, E-marketing', 'Information Technology and Telecommunications'),
             ('Programming, Consultancy', 'Information Technology and Telecommunications'),
             ('Software, Hardware', 'Information Technology and Telecommunications'),
             ('telecommunications', 'Information Technology and Telecommunications'),

             ('Air', 'Transport and Logistics'),
             ('Rail', 'Transport and Logistics'),
             ('Road', 'Transport and Logistics'),
             ('Water', 'Transport and Logistics')) v(name, parent_name)
         JOIN sectors p ON p.name = v.parent_name
WHERE NOT EXISTS (SELECT 1
                  FROM sectors s
                  WHERE s.name = v.name);

INSERT INTO sectors (name, parent_id)
SELECT v.name, p.id
FROM (VALUES ('Aluminium and steel workboats', 'Maritime'),
             ('Boat/Yacht building', 'Maritime'),
             ('Ship repair and conversion', 'Maritime'),

             ('CNC-machining', 'Metal works'),
             ('Forgings, Fasteners', 'Metal works'),
             ('Gas, Plasma, Laser cutting', 'Metal works'),
             ('MIG, TIG, Aluminium welding', 'Metal works'),

             ('Bowing', 'Plastic processing technology'),
             ('Moulding', 'Plastic processing technology'),
             ('Plastics welding and processing', 'Plastic processing technology')) v(name, parent_name)
         JOIN sectors p ON p.name = v.parent_name
WHERE NOT EXISTS (SELECT 1
                  FROM sectors s
                  WHERE s.name = v.name);
