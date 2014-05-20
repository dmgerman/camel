begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.metrics.timer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|metrics
operator|.
name|timer
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|MatcherAssert
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|notNullValue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|nullValue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|times
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
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
name|Exchange
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
name|metrics
operator|.
name|timer
operator|.
name|TimerEndpoint
operator|.
name|TimerAction
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
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|InOrder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|runners
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|MetricRegistry
import|;
end_import

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|Timer
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|TimerProducerTest
specifier|public
class|class
name|TimerProducerTest
block|{
DECL|field|METRICS_NAME
specifier|private
specifier|static
specifier|final
name|String
name|METRICS_NAME
init|=
literal|"metrics.name"
decl_stmt|;
DECL|field|PROPERTY_NAME
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_NAME
init|=
name|TimerEndpoint
operator|.
name|ENDPOINT_URI
operator|+
literal|":"
operator|+
name|METRICS_NAME
decl_stmt|;
annotation|@
name|Mock
DECL|field|endpoint
specifier|private
name|TimerEndpoint
name|endpoint
decl_stmt|;
annotation|@
name|Mock
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
annotation|@
name|Mock
DECL|field|registry
specifier|private
name|MetricRegistry
name|registry
decl_stmt|;
annotation|@
name|Mock
DECL|field|timer
specifier|private
name|Timer
name|timer
decl_stmt|;
annotation|@
name|Mock
DECL|field|context
specifier|private
name|Timer
operator|.
name|Context
name|context
decl_stmt|;
DECL|field|producer
specifier|private
name|TimerProducer
name|producer
decl_stmt|;
annotation|@
name|Mock
DECL|field|inOrder
specifier|private
name|InOrder
name|inOrder
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
name|producer
operator|=
operator|new
name|TimerProducer
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|inOrder
operator|=
name|Mockito
operator|.
name|inOrder
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|registry
argument_list|,
name|timer
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|getRegistry
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|getMetricsName
argument_list|(
name|exchange
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|registry
operator|.
name|timer
argument_list|(
name|METRICS_NAME
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|timer
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|timer
operator|.
name|time
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTimerProducer ()
specifier|public
name|void
name|testTimerProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|producer
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|producer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|equals
argument_list|(
name|endpoint
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProcessStart ()
specifier|public
name|void
name|testProcessStart
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|endpoint
operator|.
name|getAction
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|TimerAction
operator|.
name|start
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|Timer
operator|.
name|Context
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getRegistry
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getMetricsName
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getAction
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|Timer
operator|.
name|Context
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|registry
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|timer
argument_list|(
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|timer
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|time
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|setProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProcessStop ()
specifier|public
name|void
name|testProcessStop
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|endpoint
operator|.
name|getAction
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|TimerAction
operator|.
name|stop
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|Timer
operator|.
name|Context
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getRegistry
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getMetricsName
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getAction
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|Timer
operator|.
name|Context
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|context
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|stop
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|removeProperty
argument_list|(
name|PROPERTY_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProcessNoAction ()
specifier|public
name|void
name|testProcessNoAction
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|endpoint
operator|.
name|getAction
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getRegistry
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getMetricsName
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getAction
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHandleStart ()
specifier|public
name|void
name|testHandleStart
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|Timer
operator|.
name|Context
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|producer
operator|.
name|handleStart
argument_list|(
name|exchange
argument_list|,
name|registry
argument_list|,
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|Timer
operator|.
name|Context
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|registry
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|timer
argument_list|(
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|timer
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|time
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|setProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHandleStartAlreadyRunning ()
specifier|public
name|void
name|testHandleStartAlreadyRunning
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|Timer
operator|.
name|Context
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|producer
operator|.
name|handleStart
argument_list|(
name|exchange
argument_list|,
name|registry
argument_list|,
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|Timer
operator|.
name|Context
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHandleStop ()
specifier|public
name|void
name|testHandleStop
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|Timer
operator|.
name|Context
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|producer
operator|.
name|handleStop
argument_list|(
name|exchange
argument_list|,
name|registry
argument_list|,
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|Timer
operator|.
name|Context
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|context
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|stop
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|removeProperty
argument_list|(
name|PROPERTY_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHandleStopContextNotFound ()
specifier|public
name|void
name|testHandleStopContextNotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|Timer
operator|.
name|Context
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|producer
operator|.
name|handleStop
argument_list|(
name|exchange
argument_list|,
name|registry
argument_list|,
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|Timer
operator|.
name|Context
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetPropertyName ()
specifier|public
name|void
name|testGetPropertyName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|producer
operator|.
name|getPropertyName
argument_list|(
name|METRICS_NAME
argument_list|)
argument_list|,
name|is
argument_list|(
name|TimerEndpoint
operator|.
name|ENDPOINT_URI
operator|+
literal|":"
operator|+
name|METRICS_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetTimerContextFromExchange ()
specifier|public
name|void
name|testGetTimerContextFromExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|Timer
operator|.
name|Context
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|producer
operator|.
name|getTimerContextFromExchange
argument_list|(
name|exchange
argument_list|,
name|PROPERTY_NAME
argument_list|)
argument_list|,
name|is
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|Timer
operator|.
name|Context
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetTimerContextFromExchangeNotFound ()
specifier|public
name|void
name|testGetTimerContextFromExchangeNotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|Timer
operator|.
name|Context
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|producer
operator|.
name|getTimerContextFromExchange
argument_list|(
name|exchange
argument_list|,
name|PROPERTY_NAME
argument_list|)
argument_list|,
name|is
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getProperty
argument_list|(
name|PROPERTY_NAME
argument_list|,
name|Timer
operator|.
name|Context
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

