FROM gitpod/workspace-full:2023-11-24-15-04-57

USER gitpod
# gettext for envsubst
RUN brew install kotlin gettext
