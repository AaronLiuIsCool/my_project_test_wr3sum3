FROM python:3.8

WORKDIR /code

COPY requirements.txt .

COPY config.json .

COPY MatchingListener.py .

RUN pip install -r requirements.txt

ENTRYPOINT ["python", "MatchingListener.py"]