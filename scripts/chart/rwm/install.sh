#!/bin/bash

helm install --name qa2-rwm --namespace base -f values-rwm-qa2.yaml .

# upgrade
# helm upgrade -f values-rwm-qa2.yaml qa2-rwm .
