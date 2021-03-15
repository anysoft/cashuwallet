package com.cashuwallet.android;

import android.app.Application;
import androidx.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.cashuwallet.android.crypto.Coin;
import com.cashuwallet.android.crypto.Coins;
import com.raugfer.crypto.mnemonic;
import com.raugfer.crypto.pair;
import com.cashuwallet.android.crypto.Sync;
import com.cashuwallet.android.db.AppDatabase;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class MainApplication extends Application {

    private static MainApplication app; { app = this; }
    public static MainApplication app() {
        return app;
    }

    private Locker locker;
    private ExecutorService exec;
    private AppDatabase mainnetdb;
    private AppDatabase testnetdb;
    private Sync mainnetSync;
    private Sync testnetSync;
    private Map<String, Integer> themes = new HashMap<>();
    private Map<String, Integer> drawable = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();

        locker = Locker.create("default", getBaseContext());

        mainnetdb = createDatabase("mainnetdb");
        testnetdb = createDatabase("testnetdb");

        themes.put("BNB", R.style.Binance_Coin);
        themes.put("BTC", R.style.Bitcoin);
        themes.put("BCH", R.style.Bitcoin_Cash);
        themes.put("BTG", R.style.Bitcoin_Gold);
        themes.put("BSV", R.style.Bitcoin_SV);
        themes.put("ADA", R.style.Cardano);
        themes.put("DASH", R.style.Dash);
        themes.put("DCR", R.style.Decred);
        themes.put("DGB", R.style.Digibyte);
        themes.put("DOGE", R.style.Dogecoin);
        themes.put("ETH", R.style.Ethereum);
        themes.put("ETC", R.style.Ethereum_Classic);
        themes.put("FTM", R.style.Fantom);
        themes.put("LSK", R.style.Lisk);
        themes.put("LTC", R.style.Litecoin);
        themes.put("NANO", R.style.Nano);
        themes.put("NEO", R.style.Neo);
        themes.put("GAS", R.style.NeoGas);
        themes.put("QTUM", R.style.Qtum);
        themes.put("XRP", R.style.Ripple);
        themes.put("XLM", R.style.Stellar);
        themes.put("TRX", R.style.Tron);
        themes.put("WAVES", R.style.Waves);
        themes.put("ZEC", R.style.Zcash);
        themes.put("ZRX@eth", R.style._0x);
        themes.put("AE@eth", R.style.Aeternity);
        themes.put("REP@eth", R.style.Augur);
        themes.put("BAT@eth", R.style.Basic_Attention_Token);
        themes.put("BNB@eth", R.style.Binance_Coin);
        themes.put("LINK@eth", R.style.Chainlink);
        themes.put("DAI@eth", R.style.Dai);
        themes.put("EOS@eth", R.style.EOS);
        themes.put("GUSD@eth", R.style.Gemini_Dollar);
        themes.put("GNT@eth", R.style.Golem);
        themes.put("MKR@eth", R.style.Maker);
        themes.put("OMG@eth", R.style.OmiseGO);
        themes.put("SAI@eth", R.style.Sai);
        themes.put("SNT@eth", R.style.Status);
        themes.put("USDT@eth", R.style.Tether);
        themes.put("USDC@eth", R.style.USD_Coin);
        themes.put("WBTC@eth", R.style.Wrapped_Bitcoin);
        themes.put("ZIL@eth", R.style.Zilliqa);
        themes.put("BAT@bsc", R.style.Basic_Attention_Token);
        themes.put("BTC@bsc", R.style.Bitcoin);
        themes.put("BCH@bsc", R.style.Bitcoin_Cash);
        themes.put("ADA@bsc", R.style.Cardano);
        themes.put("LINK@bsc", R.style.Chainlink);
        themes.put("DAI@bsc", R.style.Dai);
        themes.put("DOGE@bsc", R.style.Dogecoin);
        themes.put("EOS@bsc", R.style.EOS);
        themes.put("ETH@bsc", R.style.Ethereum);
        themes.put("ETC@bsc", R.style.Ethereum_Classic);
        themes.put("LTC@bsc", R.style.Litecoin);
        themes.put("MKR@bsc", R.style.Maker);
        themes.put("XRP@bsc", R.style.Ripple);
        themes.put("USDT@bsc", R.style.Tether);
        themes.put("USDC@bsc", R.style.USD_Coin);
        themes.put("ZEC@bsc", R.style.Zcash);

        drawable.put("BNB", R.drawable.binancecoin);
        drawable.put("BTC", R.drawable.bitcoin);
        drawable.put("BCH", R.drawable.bitcoincash);
        drawable.put("BTG", R.drawable.bitcoingold);
        drawable.put("BSV", R.drawable.bitcoinsv);
        drawable.put("ADA", R.drawable.cardano);
        drawable.put("DASH", R.drawable.dash);
        drawable.put("DCR", R.drawable.decred);
        drawable.put("DGB", R.drawable.digibyte);
        drawable.put("DOGE", R.drawable.dogecoin);
        drawable.put("ETH", R.drawable.ethereum);
        drawable.put("ETC", R.drawable.ethereumclassic);
        drawable.put("FTM", R.drawable.fantom);
        drawable.put("LSK", R.drawable.lisk);
        drawable.put("LTC", R.drawable.litecoin);
        drawable.put("NANO", R.drawable.nano);
        drawable.put("NEO", R.drawable.neo);
        drawable.put("GAS", R.drawable.neogas);
        drawable.put("QTUM", R.drawable.qtum);
        drawable.put("XRP", R.drawable.ripple);
        drawable.put("XLM", R.drawable.stellar);
        drawable.put("TRX", R.drawable.tron);
        drawable.put("WAVES", R.drawable.waves);
        drawable.put("ZEC", R.drawable.zcash);
        drawable.put("ZRX@eth", R.drawable._0x);
        drawable.put("AE@eth", R.drawable.aeternity);
        drawable.put("REP@eth", R.drawable.augur);
        drawable.put("BAT@eth", R.drawable.basicattentiontoken);
        drawable.put("BNB@eth", R.drawable.binancecoin);
        drawable.put("LINK@eth", R.drawable.chainlink);
        drawable.put("DAI@eth", R.drawable.dai);
        drawable.put("EOS@eth", R.drawable.eos);
        drawable.put("GUSD@eth", R.drawable.geminidollar);
        drawable.put("GNT@eth", R.drawable.golem);
        drawable.put("MKR@eth", R.drawable.maker);
        drawable.put("OMG@eth", R.drawable.omisego);
        drawable.put("SAI@eth", R.drawable.sai);
        drawable.put("SNT@eth", R.drawable.status);
        drawable.put("USDT@eth", R.drawable.tether);
        drawable.put("USDC@eth", R.drawable.usdcoin);
        drawable.put("WBTC@eth", R.drawable.wrappedbitcoin);
        drawable.put("ZIL@eth", R.drawable.zilliqa);
        drawable.put("BAT@bsc", R.drawable.basicattentiontoken);
        drawable.put("BTC@bsc", R.drawable.bitcoin);
        drawable.put("BCH@bsc", R.drawable.bitcoincash);
        drawable.put("ADA@bsc", R.drawable.cardano);
        drawable.put("LINK@bsc", R.drawable.chainlink);
        drawable.put("DAI@bsc", R.drawable.dai);
        drawable.put("DOGE@bsc", R.drawable.dogecoin);
        drawable.put("EOS@bsc", R.drawable.eos);
        drawable.put("ETH@bsc", R.drawable.ethereum);
        drawable.put("ETC@bsc", R.drawable.ethereumclassic);
        drawable.put("LTC@bsc", R.drawable.litecoin);
        drawable.put("MKR@bsc", R.drawable.maker);
        drawable.put("XRP@bsc", R.drawable.ripple);
        drawable.put("USDT@bsc", R.drawable.tether);
        drawable.put("USDC@bsc", R.drawable.usdcoin);
        drawable.put("ZEC@bsc", R.drawable.zcash);

        exec = createExec();
        mainnetSync = new Sync(exec, mainnetdb.appDao(), false);
        testnetSync = new Sync(exec, testnetdb.appDao(), true);
    }

    private AppDatabase createDatabase(String name) {
        return Room.databaseBuilder(getApplicationContext(), AppDatabase.class, name).allowMainThreadQueries().build();
    }

    private ExecutorService createExec() {
        return new ThreadPoolExecutor(50, 100, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    public ExecutorService getExec() {
        return exec;
    }

    public Sync getMainnetSync() {
        return mainnetSync;
    }

    public Sync getTestnetSync() {
        return testnetSync;
    }

    public Sync getSync() {
        boolean testnet = getPreferences().getBoolean("testnet_mode", false);
        return testnet ? testnetSync : mainnetSync;
    }

    public int findTheme(String code) {
        return themes.get(code);
    }

    public int findDrawable(String code) {
        return drawable.get(code);
    }

    public boolean shuttingDown() {
        return exec.isShutdown();
    }

    public boolean networkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null) return false;
        return netInfo.isConnected();
    }

    public void signup(String[] wordlist, String words, String password, Continuation<Boolean> cont, Locker.UserAuthenticationHandler handler) {
        pair<BigInteger, Integer> t;
        try {
            t = mnemonic.unmnemonic(words, wordlist);
        } catch (IllegalArgumentException e) {
            cont.cont(false);
            return;
        }
        BigInteger entropy = t.l;
        int entropySize = t.r;
        Object[] result = new Object[2];
        mainnetSync.derive(words, password, null, result, () -> {
            Object secrets = result[0];
            BigInteger identity = (BigInteger) result[1];
            Session session = new Session(entropy, entropySize, identity);
            String plain = session.toString();
            locker.encrypt(plain, handler, (String encrypted) -> {
                if (encrypted == null) {
                    cont.cont(false);
                    return;
                }
                mainnetSync.bootstrap(secrets, () -> testnetSync.bootstrap(secrets, () -> {
                    SharedPreferences preferences = getPreferences();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("encrypted_session", encrypted);
                    editor.apply();
                    cont.cont(true);
                }));
            });
        });
    }

    public void login(Continuation<Boolean> cont, Locker.UserAuthenticationHandler handler) {
        SharedPreferences preferences = getPreferences();
        String encrypted = preferences.getString("encrypted_session", "");
        locker.decrypt(encrypted, handler, (String plain) -> {
            if (plain == null) {
                logout();
                cont.cont(false);
                return;
            }
            Session session = Session.parse(plain);
            if (session == null) {
                logout();
                cont.cont(false);
                return;
            }
            cont.cont(true);
        });
    }

    public void authenticate(String[] wordlist, String password, Coin coin, Continuation<Object> cont, Locker.UserAuthenticationHandler handler) {
        SharedPreferences preferences = getPreferences();
        String encrypted = preferences.getString("encrypted_session", "");
        locker.decrypt(encrypted, handler, (String plain) -> {
            if (plain == null) {
                cont.cont(null);
                return;
            }
            Session session = Session.parse(plain);
            if (session == null) {
                cont.cont(null);
                return;
            }
            String words = mnemonic.mnemonic(session.getEntropy(), session.getEntropySize(), wordlist);
            List<Coin> coins = new ArrayList<>();
            coins.add(coin);
            Object[] result = new Object[2];
            mainnetSync.derive(words, password, coins, result, () -> {
                Object secrets = result[0];
                BigInteger identity = (BigInteger) result[1];
                if (!identity.equals(session.getIdentity())) {
                    cont.cont(null);
                    return;
                }
                cont.cont(secrets);
            });
        });
    }

    public void logout() {
        boolean done = false;
        do {
            exec.shutdownNow();
            try {
                done = exec.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                continue;
            }
        } while (!done);
        mainnetdb.clearAllTables();
        testnetdb.clearAllTables();
        SharedPreferences preferences = getPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("encrypted_session");
        editor.apply();
        exec = createExec();
        mainnetSync = new Sync(exec, mainnetdb.appDao(), false);
        testnetSync = new Sync(exec, testnetdb.appDao(), true);
    }

    public boolean requiresReconnect() {
        int coin_count = Coins.count();
        SharedPreferences preferences = getPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        int supported_coin_count = preferences.getInt("supported_coin_count", coin_count);
        editor.putInt("supported_coin_count", coin_count);
        editor.apply();
        return supported_coin_count < coin_count;
    }

    private static class Session {
        static Session parse(String string) {
            String[] parts = string.split(":");
            if (parts.length != 3) return null;
            BigInteger entropy;
            try {
                entropy = new BigInteger(parts[0], Character.MAX_RADIX);
            } catch (NumberFormatException e) {
                return null;
            }
            int entropySize;
            try {
                entropySize = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                return null;
            }
            BigInteger identity;
            try {
                identity = new BigInteger(parts[2], Character.MAX_RADIX);
            } catch (NumberFormatException e) {
                return null;
            }
            return new Session(entropy, entropySize, identity);
        }
        private BigInteger entropy;
        private int entropySize;
        private BigInteger identity;
        Session(BigInteger entropy, int entropySize, BigInteger identity) {
            this.entropy = entropy;
            this.entropySize = entropySize;
            this.identity = identity;
        }
        BigInteger getEntropy() {
            return entropy;
        }
        int getEntropySize() {
            return entropySize;
        }
        BigInteger getIdentity() {
            return identity;
        }
        public String toString() {
            return entropy.toString(Character.MAX_RADIX) + ":" + entropySize + ":" + identity.toString(Character.MAX_RADIX);
        }
    }

}
