begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.infinispan.processor.idempotent
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
operator|.
name|processor
operator|.
name|idempotent
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|spi
operator|.
name|IdempotentRepository
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
name|support
operator|.
name|ServiceSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|commons
operator|.
name|api
operator|.
name|BasicCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|commons
operator|.
name|api
operator|.
name|BasicCacheContainer
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

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Infinispan based message id repository"
argument_list|)
DECL|class|InfinispanIdempotentRepository
specifier|public
class|class
name|InfinispanIdempotentRepository
extends|extends
name|ServiceSupport
implements|implements
name|IdempotentRepository
argument_list|<
name|Object
argument_list|>
block|{
DECL|field|cacheName
specifier|private
specifier|final
name|String
name|cacheName
decl_stmt|;
DECL|field|cacheContainer
specifier|private
specifier|final
name|BasicCacheContainer
name|cacheContainer
decl_stmt|;
DECL|field|isManagedCacheContainer
specifier|private
name|boolean
name|isManagedCacheContainer
decl_stmt|;
DECL|method|InfinispanIdempotentRepository (BasicCacheContainer cacheContainer, String cacheName)
specifier|public
name|InfinispanIdempotentRepository
parameter_list|(
name|BasicCacheContainer
name|cacheContainer
parameter_list|,
name|String
name|cacheName
parameter_list|)
block|{
name|this
operator|.
name|cacheContainer
operator|=
name|cacheContainer
expr_stmt|;
name|this
operator|.
name|cacheName
operator|=
name|cacheName
expr_stmt|;
block|}
DECL|method|InfinispanIdempotentRepository (String cacheName)
specifier|public
name|InfinispanIdempotentRepository
parameter_list|(
name|String
name|cacheName
parameter_list|)
block|{
name|cacheContainer
operator|=
operator|new
name|DefaultCacheManager
argument_list|()
expr_stmt|;
name|this
operator|.
name|cacheName
operator|=
name|cacheName
expr_stmt|;
name|isManagedCacheContainer
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|InfinispanIdempotentRepository ()
specifier|public
name|InfinispanIdempotentRepository
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|infinispanIdempotentRepository ( BasicCacheContainer cacheContainer, String processorName)
specifier|public
specifier|static
name|InfinispanIdempotentRepository
name|infinispanIdempotentRepository
parameter_list|(
name|BasicCacheContainer
name|cacheContainer
parameter_list|,
name|String
name|processorName
parameter_list|)
block|{
return|return
operator|new
name|InfinispanIdempotentRepository
argument_list|(
name|cacheContainer
argument_list|,
name|processorName
argument_list|)
return|;
block|}
DECL|method|infinispanIdempotentRepository (String processorName)
specifier|public
specifier|static
name|InfinispanIdempotentRepository
name|infinispanIdempotentRepository
parameter_list|(
name|String
name|processorName
parameter_list|)
block|{
return|return
operator|new
name|InfinispanIdempotentRepository
argument_list|(
name|processorName
argument_list|)
return|;
block|}
DECL|method|infinispanIdempotentRepository ()
specifier|public
specifier|static
name|InfinispanIdempotentRepository
name|infinispanIdempotentRepository
parameter_list|()
block|{
return|return
operator|new
name|InfinispanIdempotentRepository
argument_list|()
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Adds the key to the store"
argument_list|)
DECL|method|add (Object key)
specifier|public
name|boolean
name|add
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|Boolean
name|put
init|=
name|getCache
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
literal|true
argument_list|)
decl_stmt|;
return|return
name|put
operator|==
literal|null
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Does the store contain the given key"
argument_list|)
DECL|method|contains (Object key)
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|getCache
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Remove the key from the store"
argument_list|)
DECL|method|remove (Object key)
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|getCache
argument_list|()
operator|.
name|remove
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The processor name"
argument_list|)
DECL|method|getCacheName ()
specifier|public
name|String
name|getCacheName
parameter_list|()
block|{
return|return
name|cacheName
return|;
block|}
DECL|method|confirm (Object key)
specifier|public
name|boolean
name|confirm
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
if|if
condition|(
name|isManagedCacheContainer
condition|)
block|{
name|cacheContainer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getCache ()
specifier|private
name|BasicCache
argument_list|<
name|Object
argument_list|,
name|Boolean
argument_list|>
name|getCache
parameter_list|()
block|{
return|return
name|cacheName
operator|!=
literal|null
condition|?
name|cacheContainer
operator|.
expr|<
name|Object
operator|,
name|Boolean
operator|>
name|getCache
argument_list|(
name|cacheName
argument_list|)
operator|:
name|cacheContainer
operator|.
expr|<
name|Object
operator|,
name|Boolean
operator|>
name|getCache
argument_list|()
return|;
block|}
block|}
end_class

end_unit

