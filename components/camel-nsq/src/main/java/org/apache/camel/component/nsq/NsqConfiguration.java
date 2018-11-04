begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.nsq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nsq
package|;
end_package

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|brainlag
operator|.
name|nsq
operator|.
name|ServerAddress
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Sets
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
name|Metadata
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
name|UriParam
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
name|UriParams
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
name|UriPath
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
name|util
operator|.
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nsq
operator|.
name|NsqConstants
operator|.
name|NSQ_DEFAULT_LOOKUP_PORT
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nsq
operator|.
name|NsqConstants
operator|.
name|NSQ_DEFAULT_PORT
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|NsqConfiguration
specifier|public
class|class
name|NsqConfiguration
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The hostnames of one or more nsqlookupd servers (consumer) or nsqd servers (producer)."
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|servers
specifier|private
name|String
name|servers
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"The NSQ topic"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|topic
specifier|private
name|String
name|topic
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"The NSQ channel"
argument_list|)
DECL|field|channel
specifier|private
name|String
name|channel
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"10"
argument_list|)
DECL|field|poolSize
specifier|private
name|int
name|poolSize
init|=
literal|10
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"4161"
argument_list|,
name|description
operator|=
literal|"The NSQ lookup server port"
argument_list|)
DECL|field|lookupServerPort
specifier|private
name|int
name|lookupServerPort
init|=
name|NSQ_DEFAULT_LOOKUP_PORT
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"4150"
argument_list|)
DECL|field|port
specifier|private
name|int
name|port
init|=
name|NSQ_DEFAULT_PORT
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"5000"
argument_list|,
name|description
operator|=
literal|"The lookup interval"
argument_list|)
DECL|field|lookupInterval
specifier|private
name|long
name|lookupInterval
init|=
literal|5000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"-1"
argument_list|,
name|description
operator|=
literal|"The requeue interval in milliseconds. A value of -1 is the server default"
argument_list|)
DECL|field|requeueInterval
specifier|private
name|long
name|requeueInterval
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|description
operator|=
literal|"Automatically finish the NSQ Message when it is retrieved from the queue and before the Exchange is processed"
argument_list|)
DECL|field|autoFinish
specifier|private
name|Boolean
name|autoFinish
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"-1"
argument_list|,
name|description
operator|=
literal|"The NSQ consumer timeout period for messages retrieved from the queue. A value of -1 is the server default"
argument_list|)
DECL|field|messageTimeout
specifier|private
name|long
name|messageTimeout
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|""
argument_list|)
DECL|field|userAgent
specifier|private
name|String
name|userAgent
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|secure
specifier|private
name|boolean
name|secure
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
comment|/*      * URL a NSQ lookup server hostname.      */
DECL|method|getServers ()
specifier|public
name|String
name|getServers
parameter_list|()
block|{
return|return
name|servers
return|;
block|}
DECL|method|setServers (String servers)
specifier|public
name|void
name|setServers
parameter_list|(
name|String
name|servers
parameter_list|)
block|{
name|this
operator|.
name|servers
operator|=
name|servers
expr_stmt|;
block|}
DECL|method|getServerAddresses ()
specifier|public
name|Set
argument_list|<
name|ServerAddress
argument_list|>
name|getServerAddresses
parameter_list|()
block|{
name|Set
argument_list|<
name|ServerAddress
argument_list|>
name|serverAddresses
init|=
name|Sets
operator|.
name|newConcurrentHashSet
argument_list|()
decl_stmt|;
name|String
index|[]
name|addresses
init|=
name|servers
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|addresses
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
index|[]
name|token
init|=
name|addresses
index|[
name|i
index|]
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|String
name|host
decl_stmt|;
name|int
name|port
decl_stmt|;
if|if
condition|(
name|token
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|host
operator|=
name|token
index|[
literal|0
index|]
expr_stmt|;
name|port
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|token
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|host
operator|=
name|token
index|[
literal|0
index|]
expr_stmt|;
name|port
operator|=
literal|0
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid address: "
operator|+
name|addresses
index|[
name|i
index|]
argument_list|)
throw|;
block|}
name|serverAddresses
operator|.
name|add
argument_list|(
operator|new
name|ServerAddress
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|serverAddresses
return|;
block|}
comment|/**      * The name of topic we want to use      */
DECL|method|getTopic ()
specifier|public
name|String
name|getTopic
parameter_list|()
block|{
return|return
name|topic
return|;
block|}
DECL|method|setTopic (String topic)
specifier|public
name|void
name|setTopic
parameter_list|(
name|String
name|topic
parameter_list|)
block|{
name|this
operator|.
name|topic
operator|=
name|topic
expr_stmt|;
block|}
comment|/**      * The name of channel we want to use      */
DECL|method|getChannel ()
specifier|public
name|String
name|getChannel
parameter_list|()
block|{
return|return
name|channel
return|;
block|}
DECL|method|setChannel (String channel)
specifier|public
name|void
name|setChannel
parameter_list|(
name|String
name|channel
parameter_list|)
block|{
name|this
operator|.
name|channel
operator|=
name|channel
expr_stmt|;
block|}
comment|/**      * Consumer pool size      */
DECL|method|getPoolSize ()
specifier|public
name|int
name|getPoolSize
parameter_list|()
block|{
return|return
name|poolSize
return|;
block|}
DECL|method|setPoolSize (int poolSize)
specifier|public
name|void
name|setPoolSize
parameter_list|(
name|int
name|poolSize
parameter_list|)
block|{
name|this
operator|.
name|poolSize
operator|=
name|poolSize
expr_stmt|;
block|}
comment|/**      * The port of the nsqdlookupd server      */
DECL|method|getLookupServerPort ()
specifier|public
name|int
name|getLookupServerPort
parameter_list|()
block|{
return|return
name|lookupServerPort
return|;
block|}
DECL|method|setLookupServerPort (int lookupServerPort)
specifier|public
name|void
name|setLookupServerPort
parameter_list|(
name|int
name|lookupServerPort
parameter_list|)
block|{
name|this
operator|.
name|lookupServerPort
operator|=
name|lookupServerPort
expr_stmt|;
block|}
comment|/**      * The port of the nsqd server      */
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
comment|/**      * The lookup retry interval      */
DECL|method|getLookupInterval ()
specifier|public
name|long
name|getLookupInterval
parameter_list|()
block|{
return|return
name|lookupInterval
return|;
block|}
DECL|method|setLookupInterval (long lookupInterval)
specifier|public
name|void
name|setLookupInterval
parameter_list|(
name|long
name|lookupInterval
parameter_list|)
block|{
name|this
operator|.
name|lookupInterval
operator|=
name|lookupInterval
expr_stmt|;
block|}
comment|/**      * The requeue interval      */
DECL|method|getRequeueInterval ()
specifier|public
name|long
name|getRequeueInterval
parameter_list|()
block|{
return|return
name|requeueInterval
return|;
block|}
DECL|method|setRequeueInterval (long requeueInterval)
specifier|public
name|void
name|setRequeueInterval
parameter_list|(
name|long
name|requeueInterval
parameter_list|)
block|{
name|this
operator|.
name|requeueInterval
operator|=
name|requeueInterval
expr_stmt|;
block|}
comment|/**      * Automatically finish the NSQ message when it is retrieved from the quese and before the Exchange is processed.      */
DECL|method|getAutoFinish ()
specifier|public
name|Boolean
name|getAutoFinish
parameter_list|()
block|{
return|return
name|autoFinish
return|;
block|}
DECL|method|setAutoFinish (Boolean autoFinish)
specifier|public
name|void
name|setAutoFinish
parameter_list|(
name|Boolean
name|autoFinish
parameter_list|)
block|{
name|this
operator|.
name|autoFinish
operator|=
name|autoFinish
expr_stmt|;
block|}
comment|/**      * The NSQ message timeout for a consumer.      */
DECL|method|getMessageTimeout ()
specifier|public
name|long
name|getMessageTimeout
parameter_list|()
block|{
return|return
name|messageTimeout
return|;
block|}
DECL|method|setMessageTimeout (long messageTimeout)
specifier|public
name|void
name|setMessageTimeout
parameter_list|(
name|long
name|messageTimeout
parameter_list|)
block|{
name|this
operator|.
name|messageTimeout
operator|=
name|messageTimeout
expr_stmt|;
block|}
DECL|method|getUserAgent ()
specifier|public
name|String
name|getUserAgent
parameter_list|()
block|{
return|return
name|userAgent
return|;
block|}
DECL|method|setUserAgent (String userAgent)
specifier|public
name|void
name|setUserAgent
parameter_list|(
name|String
name|userAgent
parameter_list|)
block|{
name|this
operator|.
name|userAgent
operator|=
name|userAgent
expr_stmt|;
block|}
comment|/**      * Set secure option indicating TLS is required      */
DECL|method|isSecure ()
specifier|public
name|boolean
name|isSecure
parameter_list|()
block|{
return|return
name|secure
return|;
block|}
DECL|method|setSecure (boolean secure)
specifier|public
name|void
name|setSecure
parameter_list|(
name|boolean
name|secure
parameter_list|)
block|{
name|this
operator|.
name|secure
operator|=
name|secure
expr_stmt|;
block|}
comment|/**      * To configure security using SSLContextParameters      */
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
DECL|method|setSslContextParameters (SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|splitServers ()
specifier|private
name|String
name|splitServers
parameter_list|()
block|{
name|StringBuilder
name|servers
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
index|[]
name|pieces
init|=
name|getServers
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|pieces
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|<
name|pieces
operator|.
name|length
operator|-
literal|1
condition|)
block|{
name|servers
operator|.
name|append
argument_list|(
name|pieces
index|[
name|i
index|]
operator|+
literal|","
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|servers
operator|.
name|append
argument_list|(
name|pieces
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|servers
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

