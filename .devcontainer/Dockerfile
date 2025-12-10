FROM mcr.microsoft.com/devcontainers/java:17

# Install additional dependencies
# gettext for envsubst
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends \
    curl \
    wget \
    git \
    unzip \
    gettext \
    libz3-jni \
    libz3-java \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Install Kotlin and Gradle using the pre-installed SDKMAN
RUN bash -c ". /usr/local/sdkman/bin/sdkman-init.sh \
&& sdk install kotlin \
&& sdk install gradle"

# Add to PATH
ENV PATH="$PATH:/usr/local/sdkman/candidates/kotlin/current/bin:/usr/local/sdkman/candidates/gradle/current/bin"

# Add SDKMAN init to zshrc if not already present
RUN grep -qxF 'source /usr/local/sdkman/bin/sdkman-init.sh' /root/.zshrc || echo 'source /usr/local/sdkman/bin/sdkman-init.sh' >> /root/.zshrc

# Set ZSH as the default shell
ENV SHELL /bin/zsh
