def commit_callback(commit):
    if commit.author_email == b"prepprightankit@gmail.com":
        commit.author_name = b"Chitvan Gupta"
        commit.author_email = b"chitvangupta01@gmail.com"
        commit.committer_name = b"Chitvan Gupta"
        commit.committer_email = b"chitvangupta01@gmail.com"
