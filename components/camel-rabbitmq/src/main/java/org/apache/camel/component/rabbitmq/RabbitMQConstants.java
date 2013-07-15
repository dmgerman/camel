begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.rabbitmq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rabbitmq
package|;
end_package

begin_comment
comment|/**  * @author Stephen Samuel  */
end_comment

begin_class
DECL|class|RabbitMQConstants
specifier|public
class|class
name|RabbitMQConstants
block|{
DECL|field|ROUTING_KEY
specifier|public
specifier|static
specifier|final
name|String
name|ROUTING_KEY
init|=
literal|"rabbitmq.ROUTING_KEY"
decl_stmt|;
DECL|field|EXCHANGE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|EXCHANGE_NAME
init|=
literal|"rabbitmq.EXCHANGE_NAME"
decl_stmt|;
DECL|field|CONTENT_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_TYPE
init|=
literal|"rabbitmq.CONTENT_TYPE"
decl_stmt|;
DECL|field|PRIORITY
specifier|public
specifier|static
specifier|final
name|String
name|PRIORITY
init|=
literal|"rabbitmq.PRIORITY"
decl_stmt|;
DECL|field|DELIVERY_TAG
specifier|public
specifier|static
specifier|final
name|String
name|DELIVERY_TAG
init|=
literal|"rabbitmq.DELIVERY_TAG"
decl_stmt|;
DECL|field|CORRELATIONID
specifier|public
specifier|static
specifier|final
name|String
name|CORRELATIONID
init|=
literal|"rabbitmq.CORRELATIONID"
decl_stmt|;
DECL|field|MESSAGE_ID
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE_ID
init|=
literal|"rabbitmq.MESSAGE_ID"
decl_stmt|;
DECL|field|DELIVERY_MODE
specifier|public
specifier|static
specifier|final
name|String
name|DELIVERY_MODE
init|=
literal|"rabbitmq.DELIVERY_MODE"
decl_stmt|;
DECL|field|USERID
specifier|public
specifier|static
specifier|final
name|String
name|USERID
init|=
literal|"rabbitmq.USERID"
decl_stmt|;
DECL|field|CLUSTERID
specifier|public
specifier|static
specifier|final
name|String
name|CLUSTERID
init|=
literal|"rabbitmq.CLUSTERID"
decl_stmt|;
DECL|field|REPLY_TO
specifier|public
specifier|static
specifier|final
name|String
name|REPLY_TO
init|=
literal|"rabbitmq.REPLY_TO"
decl_stmt|;
DECL|field|CONTENT_ENCODING
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_ENCODING
init|=
literal|"rabbitmq.CONTENT_ENCODING"
decl_stmt|;
DECL|field|TYPE
specifier|public
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"rabbitmq.TYPE"
decl_stmt|;
DECL|field|EXPIRATION
specifier|public
specifier|static
specifier|final
name|String
name|EXPIRATION
init|=
literal|"rabbitmq.EXPIRATION"
decl_stmt|;
DECL|field|TIMESTAMP
specifier|public
specifier|static
specifier|final
name|String
name|TIMESTAMP
init|=
literal|"rabbitmq.TIMESTAMP"
decl_stmt|;
DECL|field|APP_ID
specifier|public
specifier|static
specifier|final
name|String
name|APP_ID
init|=
literal|"rabbitmq.APP_ID"
decl_stmt|;
block|}
end_class

end_unit

