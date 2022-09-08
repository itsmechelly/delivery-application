Hi Rotem,

First, it is important for me to say thank you for the opportunity.
I looked at the company's profile,  and I totally want to join your team!

I'm still on the time frame, but due to previous commitments I'm submitting at the last minute, I hope you will understand and will like my code. (:


---

Some Important Notes:

• Spring WebFlux -
The project contains concurrent features, I think that the optimal project will be by using Spring WebFlux, but due to time limitations I decided to use Spring Boot.

• Comments -
In addition, due to the time limit - I didn't add comments.
I believe in cleanCode that will be accessible for the team, just didn't have enough time.

but!
I include a lot of logs - in which you can understand the flow of the code easily.

• HolidayAPI -
• Since 'holidayapi' does not support the current year for free - I hardcoded data of last year - '2021'.

• Gighub bucket4j -
Regarding business capacity, to make sure that the system supports 10 deliveries per day, I used Github's amazing library called bucket4j.

• Geoapify -
To resolve the correct address I have used Geoapify.

• Timeslots limitation -
I have added a new column to Timeslot entity 'timeslotLimit'.

• Concurrent Requests Validation -
Well, Spring is Singleton by default, BUT not a thread safe.
To handle concurrent requests I used @Version annotation in the Delivery entity.
PLUS - I added ThreadPoolTaskExecutor, to setMaxPoolSize, I know I didn't have to add it, because I have already handled the 10 daily requests per day, but was thinking it would be nice to show you I can do it. (:
PLUS - I have already used @Transactional annotation for other reasons, but it gives extra support for thread safe running.


Hope to hear from you soon,
Thank you,
Chelly Izraelov