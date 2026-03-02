package com.uvod.catalog.config;

import com.uvod.catalog.model.Film;
import com.uvod.catalog.repository.FilmRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

/**
 * Populates the database with dummy films on application startup.
 */
@Component
public class CatalogSeeder implements CommandLineRunner {

        private static final Logger logger = LoggerFactory.getLogger(CatalogSeeder.class);

        private final FilmRepo filmRepo;

        public CatalogSeeder(FilmRepo filmRepo) {
                this.filmRepo = filmRepo;
        }

        @Override
        public void run(String... args) {
                long count = filmRepo.count();
                if (count > 0) {
                        logger.info("Catalog already populated ({} films). Seeding skipped.", count);
                        return;
                }

                logger.info("Collection empty. Starting catalog seeding...");

                List<Film> films = createSeedData();

                // Save one by one to handle different partition keys
                for (Film film : films) {
                        filmRepo.save(film);
                }

                logger.info("Seeding completed: {} films inserted.", films.size());
        }

        private List<Film> createSeedData() {
                // U-VoD brand palette for thumbnails
                // Dark purple background: 6c3fc5 | Indigo background: 4a2fa0 | Light blue
                // background: 3a86c8
                // White text: ffffff

                return List.of(
                                // --- SCI-FI (5 films) ---
                                buildFilm("Quantum Horizon",
                                                "In un futuro dove la realtà è frammentata, un fisico scopre che i confini tra universi paralleli stanno collassando. Dovrà scegliere quale versione della sua vita salvare.",
                                                2024, "Sci-Fi", 148, "quantum-horizon-2024.mp4",
                                                List.of("Alex Rivera", "Mia Chen", "Jordan Blake"),
                                                List.of("futuro", "universi paralleli", "fisica quantistica"),
                                                "6c3fc5"),

                                buildFilm("Neon Abyss",
                                                "Una squadra di esploratori spaziali si ritrova intrappolata in una stazione orbitale abbandonata, dove un'intelligenza artificiale li mette alla prova.",
                                                2023, "Sci-Fi", 132, "neon-abyss-2023.mp4",
                                                List.of("Kenji Tanaka", "Sara Novak", "Leo Marshall"),
                                                List.of("spazio", "intelligenza artificiale", "sopravvivenza"),
                                                "4a2fa0"),

                                buildFilm("The Last Signal",
                                                "Quando un segnale misterioso viene captato dalla sonda Voyager 3, l'umanità si divide tra chi vuole rispondere e chi teme le conseguenze.",
                                                2025, "Sci-Fi", 156, "the-last-signal-2025.mp4",
                                                List.of("David Okonkwo", "Elena Rossi", "Hank Patel"),
                                                List.of("primo contatto", "sonda spaziale", "tensione politica"),
                                                "6c3fc5"),

                                buildFilm("Chrome Veins",
                                                "In una metropoli cyberpunk, un hacker con impianti illegali scopre una cospirazione che potrebbe cancellare la coscienza digitale di milioni di persone.",
                                                2024, "Sci-Fi", 121, "chrome-veins-2024.mp4",
                                                List.of("Yuki Saito", "Marcus Webb", "Anya Kraft"),
                                                List.of("cyberpunk", "hacker", "distopia"),
                                                "4a2fa0"),

                                buildFilm("Solaris Drift",
                                                "Un equipaggio alla deriva nello spazio profondo inizia a sperimentare visioni del proprio passato, mentre la nave sembra avere una volontà propria.",
                                                2023, "Sci-Fi", 139, "solaris-drift-2023.mp4",
                                                List.of("Ingrid Larsen", "Tomás Herrera", "Faye Wong"),
                                                List.of("spazio profondo", "psicologico", "isolamento"),
                                                "6c3fc5"),

                                // --- THRILLER (5 films) ---
                                buildFilm("Sotto la Superficie",
                                                "Un avvocato di successo riceve una lettera anonima che rivela un segreto sepolto da vent'anni, innescando una spirale di ricatti e tradimenti.",
                                                2025, "Thriller", 118, "sotto-la-superficie-2025.mp4",
                                                List.of("Marco Bianchi", "Giulia Ferri", "Roberto Conti"),
                                                List.of("ricatto", "segreti", "legale"),
                                                "3a86c8"),

                                buildFilm("The Watcher",
                                                "Una famiglia si trasferisce nella casa dei sogni, ma inquietanti lettere anonime rivelano che qualcuno li osserva. La paranoia diventa il nemico peggiore.",
                                                2024, "Thriller", 109, "the-watcher-2024.mp4",
                                                List.of("Emma Stone", "Liam Harris", "Clara Jensen"),
                                                List.of("stalking", "paranoia", "famiglia"),
                                                "2d5f8a"),

                                buildFilm("Double Blind",
                                                "Due agenti segreti scoprono di avere lo stesso obiettivo ma mandanti opposti. Tra depistaggi e alleanze fragili, solo uno potrà portare a termine la missione.",
                                                2023, "Thriller", 134, "double-blind-2023.mp4",
                                                List.of("Nikolai Volkov", "Sophie Durand", "James Kato"),
                                                List.of("spionaggio", "doppio gioco", "azione"),
                                                "3a86c8"),

                                buildFilm("Fog Line",
                                                "Un treno notturno attraversa la nebbia della pianura padana. Quando un passeggero viene trovato morto, tutti diventano sospetti.",
                                                2024, "Thriller", 97, "fog-line-2024.mp4",
                                                List.of("Antonio Ferrara", "Marta Lucchini", "Hans Gruber"),
                                                List.of("mistero", "treno", "omicidio"),
                                                "2d5f8a"),

                                buildFilm("Zero Hour",
                                                "Un attacco informatico paralizza le infrastrutture di un'intera nazione. Un team di cyber-esperti ha 12 ore per trovare il responsabile prima del blackout totale.",
                                                2025, "Thriller", 126, "zero-hour-2025.mp4",
                                                List.of("Rachel Kim", "Omar Diallo", "Viktor Nowak"),
                                                List.of("cyber attacco", "countdown", "tensione"),
                                                "3a86c8"),

                                // --- DRAMA (5 films) ---
                                buildFilm("La Strada di Casa",
                                                "Dopo trent'anni all'estero, un emigrante torna nel suo paese d'origine per riscoprire le radici che aveva abbandonato e i legami che credeva perduti.",
                                                2024, "Drama", 142, "la-strada-di-casa-2024.mp4",
                                                List.of("Giuseppe Moretti", "Lucia Ferraro", "Franco Mancini"),
                                                List.of("emigrazione", "famiglia", "ritorno"),
                                                "7b2ff7"),

                                buildFilm("Still Waters",
                                                "Una pittrice in crisi creativa si ritira in un cottage isolato in Irlanda, dove un incontro inaspettato riaccende la sua ispirazione e il suo coraggio.",
                                                2023, "Drama", 115, "still-waters-2023.mp4",
                                                List.of("Aoife Brennan", "Daniel Park", "Miriam Shah"),
                                                List.of("arte", "solitudine", "rinascita"),
                                                "5a3fa8"),

                                buildFilm("Il Peso delle Parole",
                                                "Un giornalista deve scegliere tra pubblicare una verità scomoda che distruggerebbe una famiglia o proteggere un segreto che sta consumando la sua coscienza.",
                                                2025, "Drama", 128, "il-peso-delle-parole-2025.mp4",
                                                List.of("Andrea De Luca", "Silvia Colombo", "Pietro Amato"),
                                                List.of("giornalismo", "etica", "dilemma morale"),
                                                "7b2ff7"),

                                buildFilm("Paper Boats",
                                                "Due fratelli separati da bambini si ritrovano da adulti in circostanze bizzarre, scoprendo che le loro vite parallele hanno seguito schemi sorprendentemente simili.",
                                                2024, "Drama", 136, "paper-boats-2024.mp4",
                                                List.of("Jun Watanabe", "Kai Watanabe", "Hana Yoshida"),
                                                List.of("fratelli", "destino", "riconciliazione"),
                                                "5a3fa8"),

                                buildFilm("Sunrise in Havana",
                                                "Un musicista jazz americano in declino trova una seconda vita a L'Avana, dove la musica ha ancora il potere di unire le persone oltre ogni barriera.",
                                                2023, "Drama", 144, "sunrise-in-havana-2023.mp4",
                                                List.of("Marcus Johnson", "Camila Reyes", "Luis Menéndez"),
                                                List.of("musica", "jazz", "cuba", "rinascita"),
                                                "7b2ff7"),

                                // --- SCI-FI (3 additional films) ---
                                buildFilm("Binary Eclipse",
                                                "Due intelligenze artificiali gemelle, progettate per collaborare, sviluppano obiettivi opposti. Il loro conflitto minaccia di spegnere la rete energetica globale.",
                                                2025, "Sci-Fi", 131, "binary-eclipse-2025.mp4",
                                                List.of("Priya Sharma", "Ethan Cole", "Zara Obi"),
                                                List.of("intelligenza artificiale", "gemelli digitali", "energia"),
                                                "4a2fa0"),

                                buildFilm("Terraform Eden",
                                                "Su Marte, la prima colonia umana scopre forme di vita microscopiche nel sottosuolo. La scelta tra colonizzazione e preservazione dividerà per sempre i coloni.",
                                                2024, "Sci-Fi", 152, "terraform-eden-2024.mp4",
                                                List.of("Anika Johansson", "Carlos Ruiz", "Mei Lin"),
                                                List.of("marte", "colonizzazione", "bioetica"),
                                                "6c3fc5"),

                                buildFilm("Neural Bloom",
                                                "Un neuroscienziato impianta memorie artificiali nei pazienti affetti da amnesia, ma quando le memorie iniziano a sovrapporsi alla realtà, i confini svaniscono.",
                                                2025, "Sci-Fi", 118, "neural-bloom-2025.mp4",
                                                List.of("Dario Fontana", "Leah Brooks", "Sven Eriksson"),
                                                List.of("neuroscienze", "memoria", "identità"),
                                                "4a2fa0"),

                                // --- THRILLER (4 additional films) ---
                                buildFilm("Vicolo Cieco",
                                                "Un tassista notturno a Napoli viene coinvolto in un rapimento quando un passeggero misterioso sale a bordo con una valigetta e un ultimatum.",
                                                2024, "Thriller", 103, "vicolo-cieco-2024.mp4",
                                                List.of("Salvatore Esposito", "Chiara Mastroianni", "Luca Ferrante"),
                                                List.of("napoli", "rapimento", "notte"),
                                                "2d5f8a"),

                                buildFilm("The Informant",
                                                "Una giornalista infiltrata in un cartello finanziario scopre che la sua fonte è un doppio agente. Ora deve decidere di chi fidarsi prima che sia troppo tardi.",
                                                2025, "Thriller", 141, "the-informant-2025.mp4",
                                                List.of("Natalie Voss", "Ibrahim Kader", "Jenna Liu"),
                                                List.of("giornalismo", "finanza", "infiltrazione"),
                                                "3a86c8"),

                                buildFilm("Silent Protocol",
                                                "Un ex analista della NSA riceve un messaggio cifrato dal suo vecchio partner, dato per morto. Decifrarlo significa riaprire un caso che qualcuno vuole dimenticato.",
                                                2023, "Thriller", 129, "silent-protocol-2023.mp4",
                                                List.of("Aaron West", "Mila Renko", "Thomas Hagen"),
                                                List.of("NSA", "crittografia", "cospirazione"),
                                                "2d5f8a"),

                                buildFilm("Midnight Mirage",
                                                "Durante una crociera di lusso nel Mediterraneo, un furto milionario mette tutti i passeggeri sotto sospetto. Ma il vero colpevole ha un piano molto più grande.",
                                                2024, "Thriller", 112, "midnight-mirage-2024.mp4",
                                                List.of("Isabella Moreau", "Rajan Kapoor", "Felix Brandt"),
                                                List.of("crociera", "furto", "lusso"),
                                                "3a86c8"),

                                // --- DRAMA (3 additional films) ---
                                buildFilm("L'Ultimo Atto",
                                                "Un regista teatrale malato terminale decide di mettere in scena un'ultima opera con il cast originale della sua prima produzione, trent'anni dopo.",
                                                2025, "Drama", 138, "ultimo-atto-2025.mp4",
                                                List.of("Vittorio Gassman Jr.", "Claudia Neri", "Enzo Ricci"),
                                                List.of("teatro", "malattia", "addio"),
                                                "7b2ff7"),

                                buildFilm("Frames of Us",
                                                "Un fotografo ritrova una scatola di negativi mai sviluppati e ricostruisce la storia d'amore dei suoi genitori, scoprendo un segreto che cambia tutto.",
                                                2024, "Drama", 119, "frames-of-us-2024.mp4",
                                                List.of("Oliver Grant", "Sofia Lindqvist", "Rosa Martinelli"),
                                                List.of("fotografia", "famiglia", "segreti"),
                                                "5a3fa8"),

                                buildFilm("Cenere e Luce",
                                                "Dopo un devastante incendio boschivo in Sardegna, una comunità rurale lotta per ricostruire non solo le case, ma i legami umani andati in frantumi.",
                                                2023, "Drama", 147, "cenere-e-luce-2023.mp4",
                                                List.of("Maria Ferraris", "Tonio Piras", "Elisa Mura"),
                                                List.of("sardegna", "incendio", "comunità", "resilienza"),
                                                "5a3fa8"));
        }

        private Film buildFilm(String title, String description, int year, String genre,
                        int duration, String blobName, List<String> cast,
                        List<String> tags, String bgColor) {
                String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8).replace("+", "%20");
                String thumbnailUrl = String.format("https://dummyimage.com/600x400/%s/ffffff&text=%s", bgColor,
                                encodedTitle);
                String slug = title.toLowerCase()
                                .replaceAll("[^a-z0-9\\s-]", "")
                                .replaceAll("\\s+", "-");

                return Film.builder()
                                .id(UUID.randomUUID().toString())
                                .slug(slug)
                                .title(title)
                                .description(description)
                                .releaseYear(year)
                                .genre(genre)
                                .durationInMinutes(duration)
                                .thumbnailUrl(thumbnailUrl)
                                .blobName(blobName)
                                .cast(cast)
                                .tags(tags)
                                .build();
        }
}
