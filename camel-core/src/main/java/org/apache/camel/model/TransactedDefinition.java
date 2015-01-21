begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|NoSuchBeanException
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
name|Service
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
name|processor
operator|.
name|WrapProcessor
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
name|CamelContextHelper
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
comment|/**  * Enables transaction on the route  *  * @version   */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"configuration"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"transacted"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|TransactedDefinition
specifier|public
class|class
name|TransactedDefinition
extends|extends
name|OutputDefinition
argument_list|<
name|TransactedDefinition
argument_list|>
block|{
comment|// TODO: Align this code with PolicyDefinition
comment|// JAXB does not support changing the ref attribute from required to optional
comment|// if we extend PolicyDefinition so we must make a copy of the class
annotation|@
name|XmlTransient
DECL|field|PROPAGATION_REQUIRED
specifier|public
specifier|static
specifier|final
name|String
name|PROPAGATION_REQUIRED
init|=
literal|"PROPAGATION_REQUIRED"
decl_stmt|;
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
name|TransactedDefinition
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|type
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|Policy
argument_list|>
name|type
init|=
name|TransactedPolicy
operator|.
name|class
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ref
specifier|protected
name|String
name|ref
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|policy
specifier|private
name|Policy
name|policy
decl_stmt|;
DECL|method|TransactedDefinition ()
specifier|public
name|TransactedDefinition
parameter_list|()
block|{     }
DECL|method|TransactedDefinition (Policy policy)
specifier|public
name|TransactedDefinition
parameter_list|(
name|Policy
name|policy
parameter_list|)
block|{
name|this
operator|.
name|policy
operator|=
name|policy
expr_stmt|;
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
literal|"Transacted["
operator|+
name|description
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|description ()
specifier|protected
name|String
name|description
parameter_list|()
block|{
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
return|return
literal|"ref:"
operator|+
name|ref
return|;
block|}
elseif|else
if|if
condition|(
name|policy
operator|!=
literal|null
condition|)
block|{
return|return
name|policy
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
literal|"transacted["
operator|+
name|description
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|isAbstract ()
specifier|public
name|boolean
name|isAbstract
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|isTopLevelOnly ()
specifier|public
name|boolean
name|isTopLevelOnly
parameter_list|()
block|{
comment|// transacted is top level as we only allow have it configured once per route
return|return
literal|true
return|;
block|}
DECL|method|getRef ()
specifier|public
name|String
name|getRef
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
DECL|method|setRef (String ref)
specifier|public
name|void
name|setRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
comment|/**      * Sets a policy type that this definition should scope within.      *<p/>      * Is used for convention over configuration situations where the policy      * should be automatic looked up in the registry and it should be based      * on this type. For instance a {@link org.apache.camel.spi.TransactedPolicy}      * can be set as type for easy transaction configuration.      *<p/>      * Will by default scope to the wide {@link Policy}      *      * @param type the policy type      */
DECL|method|setType (Class<? extends Policy> type)
specifier|public
name|void
name|setType
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Policy
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
comment|/**      * Sets a reference to use for lookup the policy in the registry.      *      * @param ref the reference      * @return the builder      */
DECL|method|ref (String ref)
specifier|public
name|TransactedDefinition
name|ref
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|setRef
argument_list|(
name|ref
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|Policy
name|policy
init|=
name|resolvePolicy
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|policy
argument_list|,
literal|"policy"
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// before wrap
name|policy
operator|.
name|beforeWrap
argument_list|(
name|routeContext
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// create processor after the before wrap
name|Processor
name|childProcessor
init|=
name|this
operator|.
name|createChildProcessor
argument_list|(
name|routeContext
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// wrap
name|Processor
name|target
init|=
name|policy
operator|.
name|wrap
argument_list|(
name|routeContext
argument_list|,
name|childProcessor
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|target
operator|instanceof
name|Service
operator|)
condition|)
block|{
comment|// wrap the target so it becomes a service and we can manage its lifecycle
name|target
operator|=
operator|new
name|WrapProcessor
argument_list|(
name|target
argument_list|,
name|childProcessor
argument_list|)
expr_stmt|;
block|}
return|return
name|target
return|;
block|}
DECL|method|resolvePolicy (RouteContext routeContext)
specifier|protected
name|Policy
name|resolvePolicy
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|policy
operator|!=
literal|null
condition|)
block|{
return|return
name|policy
return|;
block|}
return|return
name|doResolvePolicy
argument_list|(
name|routeContext
argument_list|,
name|getRef
argument_list|()
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|doResolvePolicy (RouteContext routeContext, String ref, Class<? extends Policy> type)
specifier|protected
specifier|static
name|Policy
name|doResolvePolicy
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|String
name|ref
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Policy
argument_list|>
name|type
parameter_list|)
block|{
comment|// explicit ref given so lookup by it
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|ref
argument_list|)
condition|)
block|{
return|return
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|ref
argument_list|,
name|Policy
operator|.
name|class
argument_list|)
return|;
block|}
comment|// no explicit reference given from user so we can use some convention over configuration here
comment|// try to lookup by scoped type
name|Policy
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
comment|// try find by type, note that this method is not supported by all registry
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|types
init|=
name|routeContext
operator|.
name|lookupByType
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|types
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// only one policy defined so use it
name|Object
name|found
init|=
name|types
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
name|type
operator|.
name|isInstance
argument_list|(
name|found
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|found
argument_list|)
return|;
block|}
block|}
block|}
comment|// for transacted routing try the default REQUIRED name
if|if
condition|(
name|type
operator|==
name|TransactedPolicy
operator|.
name|class
condition|)
block|{
comment|// still not found try with the default name PROPAGATION_REQUIRED
name|answer
operator|=
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
expr_stmt|;
block|}
comment|// this logic only applies if we are a transacted policy
comment|// still no policy found then try lookup the platform transaction manager and use it as policy
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|type
operator|==
name|TransactedPolicy
operator|.
name|class
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|tmClazz
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
literal|"org.springframework.transaction.PlatformTransactionManager"
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmClazz
operator|!=
literal|null
condition|)
block|{
comment|// see if we can find the platform transaction manager in the registry
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|maps
init|=
name|routeContext
operator|.
name|lookupByType
argument_list|(
name|tmClazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|maps
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// only one platform manager then use it as default and create a transacted
comment|// policy with it and default to required
comment|// as we do not want dependency on spring jars in the camel-core we use
comment|// reflection to lookup classes and create new objects and call methods
comment|// as this is only done during route building it does not matter that we
comment|// use reflection as performance is no a concern during route building
name|Object
name|transactionManager
init|=
name|maps
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"One instance of PlatformTransactionManager found in registry: {}"
argument_list|,
name|transactionManager
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|txClazz
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
literal|"org.apache.camel.spring.spi.SpringTransactionPolicy"
argument_list|)
decl_stmt|;
if|if
condition|(
name|txClazz
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating a new temporary SpringTransactionPolicy using the PlatformTransactionManager: {}"
argument_list|,
name|transactionManager
argument_list|)
expr_stmt|;
name|TransactedPolicy
name|txPolicy
init|=
name|ObjectHelper
operator|.
name|newInstance
argument_list|(
name|txClazz
argument_list|,
name|TransactedPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
name|Method
name|method
decl_stmt|;
try|try
block|{
name|method
operator|=
name|txClazz
operator|.
name|getMethod
argument_list|(
literal|"setTransactionManager"
argument_list|,
name|tmClazz
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot get method setTransactionManager(PlatformTransactionManager) on class: "
operator|+
name|txClazz
argument_list|)
throw|;
block|}
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|method
argument_list|,
name|txPolicy
argument_list|,
name|transactionManager
argument_list|)
expr_stmt|;
return|return
name|txPolicy
return|;
block|}
else|else
block|{
comment|// camel-spring is missing on the classpath
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot create a transacted policy as camel-spring.jar is not on the classpath!"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|maps
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchBeanException
argument_list|(
literal|null
argument_list|,
literal|"PlatformTransactionManager"
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Found "
operator|+
name|maps
operator|.
name|size
argument_list|()
operator|+
literal|" PlatformTransactionManager in registry. "
operator|+
literal|"Cannot determine which one to use. Please configure a TransactionTemplate on the transacted policy."
argument_list|)
throw|;
block|}
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

