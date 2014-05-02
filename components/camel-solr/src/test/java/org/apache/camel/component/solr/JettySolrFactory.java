begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.solr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|solr
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyManagementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStoreException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|HttpsURLConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLSocketFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|TrustManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|X509TrustManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|conn
operator|.
name|ssl
operator|.
name|SSLConnectionSocketFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|conn
operator|.
name|ssl
operator|.
name|SSLContextBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|conn
operator|.
name|ssl
operator|.
name|TrustSelfSignedStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|util
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|solr
operator|.
name|client
operator|.
name|solrj
operator|.
name|embedded
operator|.
name|JettySolrRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|solr
operator|.
name|client
operator|.
name|solrj
operator|.
name|embedded
operator|.
name|SSLConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|solr
operator|.
name|client
operator|.
name|solrj
operator|.
name|impl
operator|.
name|HttpClientConfigurer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|servlet
operator|.
name|ServletHolder
import|;
end_import

begin_comment
comment|// Create embedded's Solrs for testing,
end_comment

begin_comment
comment|// based on SolrJettyTestBase
end_comment

begin_class
DECL|class|JettySolrFactory
specifier|public
class|class
name|JettySolrFactory
block|{
DECL|field|TEST_KEYSTORE
specifier|public
specifier|static
name|File
name|TEST_KEYSTORE
init|=
operator|new
name|File
argument_list|(
literal|"./target/test-classes/solrtest.keystore"
argument_list|)
decl_stmt|;
DECL|field|mockedSslClient
specifier|private
specifier|static
name|boolean
name|mockedSslClient
init|=
literal|false
decl_stmt|;
DECL|field|TEST_KEYSTORE_PATH
specifier|private
specifier|static
name|String
name|TEST_KEYSTORE_PATH
init|=
name|TEST_KEYSTORE
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
DECL|field|TEST_KEYSTORE_PASSWORD
specifier|private
specifier|static
name|String
name|TEST_KEYSTORE_PASSWORD
init|=
literal|"secret"
decl_stmt|;
DECL|field|DEFAULT_CONFIGURER
specifier|private
specifier|static
name|HttpClientConfigurer
name|DEFAULT_CONFIGURER
init|=
operator|new
name|HttpClientConfigurer
argument_list|()
decl_stmt|;
DECL|method|buildSSLConfig (boolean useSsl, boolean sslClientAuth)
specifier|private
specifier|static
name|SSLConfig
name|buildSSLConfig
parameter_list|(
name|boolean
name|useSsl
parameter_list|,
name|boolean
name|sslClientAuth
parameter_list|)
block|{
name|SSLConfig
name|sslConfig
init|=
operator|new
name|SSLConfig
argument_list|(
name|useSsl
argument_list|,
literal|false
argument_list|,
name|TEST_KEYSTORE_PATH
argument_list|,
name|TEST_KEYSTORE_PASSWORD
argument_list|,
name|TEST_KEYSTORE_PATH
argument_list|,
name|TEST_KEYSTORE_PASSWORD
argument_list|)
decl_stmt|;
return|return
name|sslConfig
return|;
block|}
DECL|method|installAllTrustingClientSsl ()
specifier|private
specifier|static
name|void
name|installAllTrustingClientSsl
parameter_list|()
throws|throws
name|KeyManagementException
throws|,
name|NoSuchAlgorithmException
throws|,
name|KeyStoreException
block|{
name|SSLContextBuilder
name|builder
init|=
operator|new
name|SSLContextBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|loadTrustMaterial
argument_list|(
literal|null
argument_list|,
operator|new
name|TrustSelfSignedStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|SSLConnectionSocketFactory
name|sslsf
init|=
operator|new
name|SSLConnectionSocketFactory
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|)
decl_stmt|;
comment|//		// Create a trust manager that does not validate certificate chains
specifier|final
name|TrustManager
index|[]
name|trustAllCerts
init|=
operator|new
name|TrustManager
index|[]
block|{
operator|new
name|X509TrustManager
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|checkClientTrusted
parameter_list|(
specifier|final
name|X509Certificate
index|[]
name|chain
parameter_list|,
specifier|final
name|String
name|authType
parameter_list|)
block|{
block|}
function|@Override 	        public void checkServerTrusted
parameter_list|(
specifier|final
name|X509Certificate
index|[]
name|chain
parameter_list|,
specifier|final
name|String
name|authType
parameter_list|)
block|{
block|}
function|@Override 	        public X509Certificate[] getAcceptedIssuers
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
function|} };
specifier|final
name|SSLContext
name|sslContext
init|=
name|SSLContext
operator|.
name|getInstance
argument_list|(
literal|"TLS"
argument_list|)
decl_stmt|;
name|sslContext
operator|.
name|init
argument_list|(
literal|null
argument_list|,
name|trustAllCerts
argument_list|,
operator|new
name|java
operator|.
name|security
operator|.
name|SecureRandom
argument_list|()
argument_list|)
expr_stmt|;
name|SSLContext
operator|.
name|setDefault
parameter_list|(
name|sslContext
parameter_list|)
constructor_decl|;
comment|//	    // Install the all-trusting trust manager
comment|//	    final SSLContext sslContext = SSLContext.getInstance( "SSL" );
comment|//	    sslContext.init( null, trustAllCerts, new java.security.SecureRandom() );
comment|//	    // Create an ssl socket factory with our all-trusting manager
comment|//	    final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
comment|//        HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
block|}
DECL|method|createJetty (String solrHome, String configFile, String schemaFile, String context, boolean stopAtShutdown, SortedMap<ServletHolder, String> extraServlets, boolean ssl)
specifier|private
specifier|static
name|JettySolrRunner
name|createJetty
parameter_list|(
name|String
name|solrHome
parameter_list|,
name|String
name|configFile
parameter_list|,
name|String
name|schemaFile
parameter_list|,
name|String
name|context
parameter_list|,
name|boolean
name|stopAtShutdown
parameter_list|,
name|SortedMap
argument_list|<
name|ServletHolder
argument_list|,
name|String
argument_list|>
name|extraServlets
parameter_list|,
name|boolean
name|ssl
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|mockedSslClient
condition|)
block|{
name|installAllTrustingClientSsl
argument_list|()
expr_stmt|;
name|mockedSslClient
operator|=
literal|true
expr_stmt|;
block|}
comment|// Set appropriate paths for Solr to use.
name|System
operator|.
name|setProperty
argument_list|(
literal|"solr.solr.home"
argument_list|,
name|solrHome
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"jetty.testMode"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"solr.data.dir"
argument_list|,
literal|"target/test-classes/solr/data"
argument_list|)
expr_stmt|;
comment|// Instruct Solr to keep the index in memory, for faster testing.
name|System
operator|.
name|setProperty
argument_list|(
literal|"solr.directoryFactory"
argument_list|,
literal|"solr.RAMDirectoryFactory"
argument_list|)
expr_stmt|;
name|SSLConfig
name|sslConfig
init|=
name|buildSSLConfig
argument_list|(
name|ssl
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|context
operator|=
name|context
operator|==
literal|null
condition|?
literal|"/solr"
else|:
name|context
expr_stmt|;
name|JettySolrRunner
name|jetty
init|=
operator|new
name|JettySolrRunner
argument_list|(
name|solrHome
argument_list|,
name|context
argument_list|,
literal|0
argument_list|,
name|configFile
argument_list|,
name|schemaFile
argument_list|,
name|stopAtShutdown
argument_list|,
name|extraServlets
argument_list|,
name|sslConfig
argument_list|)
decl_stmt|;
name|jetty
operator|.
name|start
argument_list|()
expr_stmt|;
name|int
name|port
init|=
name|jetty
operator|.
name|getLocalPort
argument_list|()
decl_stmt|;
return|return
name|jetty
return|;
block|}
DECL|method|createJettyTestFixture (boolean useSsl)
function|public static JettySolrRunner createJettyTestFixture
parameter_list|(
name|boolean
name|useSsl
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|solrHome
init|=
literal|"src/test/resources/solr"
decl_stmt|;
name|String
name|configFile
init|=
literal|null
decl_stmt|;
name|String
name|schemaFile
init|=
literal|null
decl_stmt|;
name|String
name|context
init|=
literal|"/solr"
decl_stmt|;
name|boolean
name|stopAtShutdown
init|=
literal|true
decl_stmt|;
name|SortedMap
argument_list|<
name|ServletHolder
argument_list|,
name|String
argument_list|>
name|extraServlets
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|useSsl
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"tests.jettySsl"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
block|}
return|return
name|createJetty
argument_list|(
name|solrHome
argument_list|,
name|configFile
argument_list|,
name|schemaFile
argument_list|,
name|context
argument_list|,
name|stopAtShutdown
argument_list|,
name|extraServlets
argument_list|,
name|useSsl
argument_list|)
return|;
block|}
function|}
end_class

end_unit

