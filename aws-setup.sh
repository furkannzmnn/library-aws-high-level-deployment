
## if aws cli is not installed, install it
## https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2-linux.html

aws configure set aws_access_key_id "test"
aws configure set aws_secret_access_key "test"
aws configure set default.region eu-west-3


aws --endpoint-url=http://localhost:4566 secretsmanager create-secret --name aws/secret --secret-string '{"my_uname":"username","my_pwd":"password"}'

aws --endpoint-url=http://localhost:4566  s3api create-bucket \
              --bucket library-folksdev \
              --region eu-west-1 \
              --create-bucket-configuration LocationConstraint=eu-west-3