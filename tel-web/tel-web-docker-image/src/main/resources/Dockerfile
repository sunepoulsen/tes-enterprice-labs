FROM @telWebBaseDockerImageName@:@telWebBaseDockerImageTag@

RUN rm -f /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d

RUN chown -R nginx:nginx /var/cache/nginx && \
    chown -R nginx:nginx /var/log/nginx && \
    chown -R nginx:nginx /etc/nginx/conf.d

RUN touch /var/run/nginx.pid && \
    chown -R nginx:nginx /var/run/nginx.pid

RUN rm -f /var/log/nginx/*

USER nginx

COPY dist /usr/share/nginx/html

EXPOSE 443
