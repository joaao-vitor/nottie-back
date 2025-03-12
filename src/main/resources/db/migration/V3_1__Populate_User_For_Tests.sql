INSERT INTO public.users (email, username, password, name, profile_img)
VALUES ('teste@gmail.com', 'teste', '$2a$10$DoFWxBuQJIUasVmAuh.rROzvZWBQk2zIa8oEiWSzYIcyPF9ssf6A6', 'teste',
        '"https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"');

INSERT INTO public.user_details (user_id, enabled)
VALUES (11, true);

INSERT INTO public.verification_token (token, expiry_date, user_id, expired, verified)
VALUES ('720fb06d-c42f-42e2-b608-583af74d82c8', '2025-03-10 05:43:52.335630', 11, true, true);
