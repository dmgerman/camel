begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.cdi.jta
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|jta
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|LoggingLevel
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
name|RuntimeCamelException
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
name|DefaultErrorHandlerBuilder
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
name|ErrorHandlerBuilder
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
name|Policy
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
name|RouteContext
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
name|TransactedPolicy
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
name|CamelLogger
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
name|ObjectHelper
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

begin_comment
comment|/**  * Builds transactional error handlers. This class is based on  * {@link org.apache.camel.spring.spi.TransactionErrorHandlerBuilder}.  */
end_comment

begin_class
DECL|class|JtaTransactionErrorHandlerBuilder
specifier|public
class|class
name|JtaTransactionErrorHandlerBuilder
extends|extends
name|DefaultErrorHandlerBuilder
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JtaTransactionErrorHandlerBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PROPAGATION_REQUIRED
specifier|private
specifier|static
specifier|final
name|String
name|PROPAGATION_REQUIRED
init|=
literal|"PROPAGATION_REQUIRED"
decl_stmt|;
DECL|field|ROLLBACK_LOGGING_LEVEL_PROPERTY
specifier|public
specifier|static
specifier|final
name|String
name|ROLLBACK_LOGGING_LEVEL_PROPERTY
init|=
name|JtaTransactionErrorHandlerBuilder
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"#rollbackLoggingLevel"
decl_stmt|;
DECL|field|rollbackLoggingLevel
specifier|private
name|LoggingLevel
name|rollbackLoggingLevel
init|=
name|LoggingLevel
operator|.
name|WARN
decl_stmt|;
DECL|field|transactionPolicy
specifier|private
name|JtaTransactionPolicy
name|transactionPolicy
decl_stmt|;
DECL|field|policyRef
specifier|private
name|String
name|policyRef
decl_stmt|;
annotation|@
name|Override
DECL|method|supportTransacted ()
specifier|public
name|boolean
name|supportTransacted
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|cloneBuilder ()
specifier|public
name|ErrorHandlerBuilder
name|cloneBuilder
parameter_list|()
block|{
specifier|final
name|JtaTransactionErrorHandlerBuilder
name|answer
init|=
operator|new
name|JtaTransactionErrorHandlerBuilder
argument_list|()
decl_stmt|;
name|cloneBuilder
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|cloneBuilder (DefaultErrorHandlerBuilder other)
specifier|protected
name|void
name|cloneBuilder
parameter_list|(
name|DefaultErrorHandlerBuilder
name|other
parameter_list|)
block|{
name|super
operator|.
name|cloneBuilder
argument_list|(
name|other
argument_list|)
expr_stmt|;
if|if
condition|(
name|other
operator|instanceof
name|JtaTransactionErrorHandlerBuilder
condition|)
block|{
specifier|final
name|JtaTransactionErrorHandlerBuilder
name|otherTx
init|=
operator|(
name|JtaTransactionErrorHandlerBuilder
operator|)
name|other
decl_stmt|;
name|transactionPolicy
operator|=
name|otherTx
operator|.
name|transactionPolicy
expr_stmt|;
name|rollbackLoggingLevel
operator|=
name|otherTx
operator|.
name|rollbackLoggingLevel
expr_stmt|;
block|}
block|}
DECL|method|createErrorHandler (final RouteContext routeContext, final Processor processor)
specifier|public
name|Processor
name|createErrorHandler
parameter_list|(
specifier|final
name|RouteContext
name|routeContext
parameter_list|,
specifier|final
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
comment|// resolve policy reference, if given
if|if
condition|(
name|transactionPolicy
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|policyRef
operator|!=
literal|null
condition|)
block|{
specifier|final
name|TransactedDefinition
name|transactedDefinition
init|=
operator|new
name|TransactedDefinition
argument_list|()
decl_stmt|;
name|transactedDefinition
operator|.
name|setRef
argument_list|(
name|policyRef
argument_list|)
expr_stmt|;
specifier|final
name|Policy
name|policy
init|=
name|transactedDefinition
operator|.
name|resolvePolicy
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|policy
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
operator|(
name|policy
operator|instanceof
name|JtaTransactionPolicy
operator|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"The configured policy '"
operator|+
name|policyRef
operator|+
literal|"' is of type '"
operator|+
name|policyRef
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"' but an instance of '"
operator|+
name|JtaTransactionPolicy
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"' is required!"
argument_list|)
throw|;
block|}
name|transactionPolicy
operator|=
operator|(
name|JtaTransactionPolicy
operator|)
name|policy
expr_stmt|;
block|}
block|}
block|}
comment|// try to lookup default policy
if|if
condition|(
name|transactionPolicy
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No tranaction policiy configured on TransactionErrorHandlerBuilder. Will try find it in the registry."
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|TransactedPolicy
argument_list|>
name|mapPolicy
init|=
name|routeContext
operator|.
name|lookupByType
argument_list|(
name|TransactedPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|mapPolicy
operator|!=
literal|null
operator|&&
name|mapPolicy
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|TransactedPolicy
name|policy
init|=
name|mapPolicy
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|policy
operator|!=
literal|null
operator|&&
name|policy
operator|instanceof
name|JtaTransactionPolicy
condition|)
block|{
name|transactionPolicy
operator|=
operator|(
operator|(
name|JtaTransactionPolicy
operator|)
name|policy
operator|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|transactionPolicy
operator|==
literal|null
condition|)
block|{
name|TransactedPolicy
name|policy
init|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|PROPAGATION_REQUIRED
argument_list|,
name|TransactedPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|policy
operator|!=
literal|null
operator|&&
name|policy
operator|instanceof
name|JtaTransactionPolicy
condition|)
block|{
name|transactionPolicy
operator|=
operator|(
operator|(
name|JtaTransactionPolicy
operator|)
name|policy
operator|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|transactionPolicy
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found TransactionPolicy in registry to use: "
operator|+
name|transactionPolicy
argument_list|)
expr_stmt|;
block|}
block|}
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|transactionPolicy
argument_list|,
literal|"transactionPolicy"
argument_list|,
name|this
argument_list|)
expr_stmt|;
specifier|final
name|CamelContext
name|camelContext
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
name|camelContext
operator|.
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|properties
operator|!=
literal|null
operator|)
operator|&&
name|properties
operator|.
name|containsKey
argument_list|(
name|ROLLBACK_LOGGING_LEVEL_PROPERTY
argument_list|)
condition|)
block|{
name|rollbackLoggingLevel
operator|=
name|LoggingLevel
operator|.
name|valueOf
argument_list|(
name|properties
operator|.
name|get
argument_list|(
name|ROLLBACK_LOGGING_LEVEL_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|JtaTransactionErrorHandler
name|answer
init|=
operator|new
name|JtaTransactionErrorHandler
argument_list|(
name|camelContext
argument_list|,
name|processor
argument_list|,
name|getLogger
argument_list|()
argument_list|,
name|getOnRedelivery
argument_list|()
argument_list|,
name|getRedeliveryPolicy
argument_list|()
argument_list|,
name|getExceptionPolicyStrategy
argument_list|()
argument_list|,
name|transactionPolicy
argument_list|,
name|getRetryWhilePolicy
argument_list|(
name|camelContext
argument_list|)
argument_list|,
name|getExecutorService
argument_list|(
name|camelContext
argument_list|)
argument_list|,
name|rollbackLoggingLevel
argument_list|,
name|getOnExceptionOccurred
argument_list|()
argument_list|)
decl_stmt|;
comment|// configure error handler before we can use it
name|configure
argument_list|(
name|routeContext
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|setTransactionPolicy (final String ref)
specifier|public
name|JtaTransactionErrorHandlerBuilder
name|setTransactionPolicy
parameter_list|(
specifier|final
name|String
name|ref
parameter_list|)
block|{
name|policyRef
operator|=
name|ref
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setTransactionPolicy (final JtaTransactionPolicy transactionPolicy)
specifier|public
name|JtaTransactionErrorHandlerBuilder
name|setTransactionPolicy
parameter_list|(
specifier|final
name|JtaTransactionPolicy
name|transactionPolicy
parameter_list|)
block|{
name|this
operator|.
name|transactionPolicy
operator|=
name|transactionPolicy
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setRollbackLoggingLevel (final LoggingLevel rollbackLoggingLevel)
specifier|public
name|JtaTransactionErrorHandlerBuilder
name|setRollbackLoggingLevel
parameter_list|(
specifier|final
name|LoggingLevel
name|rollbackLoggingLevel
parameter_list|)
block|{
name|this
operator|.
name|rollbackLoggingLevel
operator|=
name|rollbackLoggingLevel
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|createLogger ()
specifier|protected
name|CamelLogger
name|createLogger
parameter_list|()
block|{
return|return
operator|new
name|CamelLogger
argument_list|(
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|TransactionErrorHandler
operator|.
name|class
argument_list|)
argument_list|,
name|LoggingLevel
operator|.
name|ERROR
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"JtaTransactionErrorHandlerBuilder"
return|;
block|}
block|}
end_class

end_unit

