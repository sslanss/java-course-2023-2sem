INSERT INTO trackings (chat_id, link_id)
VALUES (1,(SELECT link_id
            FROM links
            WHERE url = 'https://github.com/sslanss/java-course-2023-2sem')),
    (2,(SELECT link_id
        FROM links
        WHERE url = 'https://stackoverflow.com/questions/78226097/problems-in-validations-via-web-service-in-a-vue-3-application')),
    (2,(SELECT link_id
              FROM links
              WHERE url = 'https://github.com/sslanss/java-course-2023-2sem'));
