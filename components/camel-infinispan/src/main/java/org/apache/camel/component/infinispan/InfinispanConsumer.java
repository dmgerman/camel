begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.infinispan
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
package|;
end_package

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
name|Processor
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
name|DefaultConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|manager
operator|.
name|DefaultCacheManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|InfinispanConsumer
specifier|public
class|class
name|InfinispanConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|InfinispanProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|InfinispanConfiguration
name|configuration
decl_stmt|;
DECL|field|listener
specifier|private
specifier|final
name|InfinispanSyncEventListener
name|listener
decl_stmt|;
DECL|field|defaultCacheManager
specifier|private
name|DefaultCacheManager
name|defaultCacheManager
decl_stmt|;
DECL|method|InfinispanConsumer (InfinispanEndpoint endpoint, Processor processor, InfinispanConfiguration configuration)
specifier|public
name|InfinispanConsumer
parameter_list|(
name|InfinispanEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|InfinispanConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|isSync
argument_list|()
condition|)
block|{
name|listener
operator|=
operator|new
name|InfinispanSyncEventListener
argument_list|(
name|this
argument_list|,
name|configuration
operator|.
name|getEventTypes
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|listener
operator|=
operator|new
name|InfinispanAsyncEventListener
argument_list|(
name|this
argument_list|,
name|configuration
operator|.
name|getEventTypes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processEvent (String eventType, boolean isPre, String cacheName, Object key)
specifier|public
name|void
name|processEvent
parameter_list|(
name|String
name|eventType
parameter_list|,
name|boolean
name|isPre
parameter_list|,
name|String
name|cacheName
parameter_list|,
name|Object
name|key
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|EVENT_TYPE
argument_list|,
name|eventType
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|IS_PRE
argument_list|,
name|isPre
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|CACHE_NAME
argument_list|,
name|cacheName
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|KEY
argument_list|,
name|key
argument_list|)
expr_stmt|;
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Error processing event "
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|configuration
operator|.
name|getCacheContainer
argument_list|()
operator|instanceof
name|DefaultCacheManager
condition|)
block|{
name|defaultCacheManager
operator|=
operator|(
name|DefaultCacheManager
operator|)
name|configuration
operator|.
name|getCacheContainer
argument_list|()
expr_stmt|;
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getCacheName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cache
operator|=
name|defaultCacheManager
operator|.
name|getCache
argument_list|(
name|configuration
operator|.
name|getCacheName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cache
operator|=
name|defaultCacheManager
operator|.
name|getCache
argument_list|()
expr_stmt|;
block|}
name|cache
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Consumer not support for CacheContainer: "
operator|+
name|configuration
operator|.
name|getCacheContainer
argument_list|()
argument_list|)
throw|;
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|defaultCacheManager
operator|!=
literal|null
condition|)
block|{
name|defaultCacheManager
operator|.
name|removeListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

