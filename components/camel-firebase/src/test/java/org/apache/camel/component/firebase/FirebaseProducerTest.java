begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.firebase
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|firebase
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|firebase
operator|.
name|database
operator|.
name|DatabaseReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|GenericFile
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|firebase
operator|.
name|provider
operator|.
name|ConfigurationProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|firebase
operator|.
name|provider
operator|.
name|SampleInputProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLEncoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|locks
operator|.
name|Condition
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|locks
operator|.
name|ReentrantLock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|AssertionsForClassTypes
operator|.
name|assertThat
import|;
end_import

begin_comment
comment|/**  * Tests two scenarios: a synchronous and one asynchronous request.  */
end_comment

begin_class
DECL|class|FirebaseProducerTest
specifier|public
class|class
name|FirebaseProducerTest
block|{
DECL|field|reentrantLock
specifier|private
specifier|final
name|ReentrantLock
name|reentrantLock
init|=
operator|new
name|ReentrantLock
argument_list|()
decl_stmt|;
DECL|field|wake
specifier|private
specifier|final
name|Condition
name|wake
init|=
name|reentrantLock
operator|.
name|newCondition
argument_list|()
decl_stmt|;
DECL|field|sampleInputProvider
specifier|private
name|SampleInputProvider
name|sampleInputProvider
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|sampleInputProvider
operator|=
operator|new
name|SampleInputProvider
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|whenFirebaseSet_ShouldReceiveMessagesSync ()
specifier|public
name|void
name|whenFirebaseSet_ShouldReceiveMessagesSync
parameter_list|()
throws|throws
name|Exception
block|{
name|startRoute
argument_list|(
literal|false
argument_list|,
name|DatabaseReference
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|whenFirebaseSet_ShouldReceiveMessagesAsync ()
specifier|public
name|void
name|whenFirebaseSet_ShouldReceiveMessagesAsync
parameter_list|()
throws|throws
name|Exception
block|{
name|startRoute
argument_list|(
literal|true
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|startRoute (final boolean async, final Class<?> expectedBodyClass)
specifier|private
name|void
name|startRoute
parameter_list|(
specifier|final
name|boolean
name|async
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|expectedBodyClass
parameter_list|)
throws|throws
name|Exception
block|{
name|sampleInputProvider
operator|.
name|copySampleFile
argument_list|()
expr_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|rootReference
init|=
name|URLEncoder
operator|.
name|encode
argument_list|(
name|ConfigurationProvider
operator|.
name|createRootReference
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|String
name|serviceAccountFile
init|=
name|ConfigurationProvider
operator|.
name|createFirebaseConfigLink
argument_list|()
decl_stmt|;
name|from
argument_list|(
name|sampleInputProvider
operator|.
name|getTargetFolder
argument_list|()
operator|.
name|toUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
lambda|->
block|{
name|GenericFile
name|file
init|=
operator|(
name|GenericFile
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|String
name|content
init|=
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|file
operator|.
name|getAbsoluteFilePath
argument_list|()
argument_list|)
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|String
index|[]
name|keyValue
init|=
name|content
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
specifier|final
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|out
operator|.
name|setHeader
argument_list|(
literal|"firebaseKey"
argument_list|,
name|keyValue
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|out
operator|.
name|setBody
argument_list|(
name|keyValue
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"firebase://%s?rootReference=%s&serviceAccountFile=%s&async=%b"
argument_list|,
name|ConfigurationProvider
operator|.
name|createDatabaseUrl
argument_list|()
argument_list|,
name|rootReference
argument_list|,
name|serviceAccountFile
argument_list|,
name|async
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:whenFirebaseSet?level=WARN"
argument_list|)
operator|.
name|process
argument_list|(
name|exchange1
lambda|->
block|{
name|assertThat
argument_list|(
name|exchange1
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedBodyClass
argument_list|)
expr_stmt|;
try|try
block|{
name|reentrantLock
operator|.
name|lock
argument_list|()
expr_stmt|;
name|wake
operator|.
name|signal
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|reentrantLock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|reentrantLock
operator|.
name|lock
argument_list|()
expr_stmt|;
name|wake
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|reentrantLock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

