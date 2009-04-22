begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PreDestroy
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InitialContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Binding
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
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
name|Route
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
name|Routes
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
name|TypeConverter
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
name|guice
operator|.
name|impl
operator|.
name|GuiceInjector
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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|JndiRegistry
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
name|ComponentResolver
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
name|ExchangeConverter
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
name|Injector
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
name|InterceptStrategy
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
name|LanguageResolver
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
name|LifecycleStrategy
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
name|Registry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|guiceyfruit
operator|.
name|Injectors
import|;
end_import

begin_comment
comment|/**  * The default CamelContext implementation for working with Guice.  *<p/>  * It is recommended you use this implementation with the  *<a href="http://code.google.com/p/guiceyfruit/wiki/GuiceyJndi">Guicey JNDI Provider</a>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|GuiceCamelContext
specifier|public
class|class
name|GuiceCamelContext
extends|extends
name|DefaultCamelContext
block|{
DECL|field|injector
specifier|private
specifier|final
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Injector
name|injector
decl_stmt|;
annotation|@
name|Inject
DECL|method|GuiceCamelContext (com.google.inject.Injector injector)
specifier|public
name|GuiceCamelContext
parameter_list|(
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Injector
name|injector
parameter_list|)
block|{
name|this
operator|.
name|injector
operator|=
name|injector
expr_stmt|;
block|}
annotation|@
name|PostConstruct
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|PreDestroy
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Inject
DECL|method|setRouteBuilders (Set<Routes> routeBuilders)
specifier|public
name|void
name|setRouteBuilders
parameter_list|(
name|Set
argument_list|<
name|Routes
argument_list|>
name|routeBuilders
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Routes
name|routeBuilder
range|:
name|routeBuilders
control|)
block|{
name|addRoutes
argument_list|(
name|routeBuilder
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setRoutes (List<Route> routes)
specifier|public
name|void
name|setRoutes
parameter_list|(
name|List
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
block|{
name|super
operator|.
name|setRoutes
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setRegistry (Registry registry)
specifier|public
name|void
name|setRegistry
parameter_list|(
name|Registry
name|registry
parameter_list|)
block|{
name|super
operator|.
name|setRegistry
argument_list|(
name|registry
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setJndiContext (Context jndiContext)
specifier|public
name|void
name|setJndiContext
parameter_list|(
name|Context
name|jndiContext
parameter_list|)
block|{
name|super
operator|.
name|setJndiContext
argument_list|(
name|jndiContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setInjector (Injector injector)
specifier|public
name|void
name|setInjector
parameter_list|(
name|Injector
name|injector
parameter_list|)
block|{
name|super
operator|.
name|setInjector
argument_list|(
name|injector
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setExchangeConverter (ExchangeConverter exchangeConverter)
specifier|public
name|void
name|setExchangeConverter
parameter_list|(
name|ExchangeConverter
name|exchangeConverter
parameter_list|)
block|{
name|super
operator|.
name|setExchangeConverter
argument_list|(
name|exchangeConverter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setComponentResolver (ComponentResolver componentResolver)
specifier|public
name|void
name|setComponentResolver
parameter_list|(
name|ComponentResolver
name|componentResolver
parameter_list|)
block|{
name|super
operator|.
name|setComponentResolver
argument_list|(
name|componentResolver
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setAutoCreateComponents (boolean autoCreateComponents)
specifier|public
name|void
name|setAutoCreateComponents
parameter_list|(
name|boolean
name|autoCreateComponents
parameter_list|)
block|{
name|super
operator|.
name|setAutoCreateComponents
argument_list|(
name|autoCreateComponents
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setErrorHandlerBuilder (ErrorHandlerBuilder errorHandlerBuilder)
specifier|public
name|void
name|setErrorHandlerBuilder
parameter_list|(
name|ErrorHandlerBuilder
name|errorHandlerBuilder
parameter_list|)
block|{
name|super
operator|.
name|setErrorHandlerBuilder
argument_list|(
name|errorHandlerBuilder
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setInterceptStrategies (List<InterceptStrategy> interceptStrategies)
specifier|public
name|void
name|setInterceptStrategies
parameter_list|(
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|interceptStrategies
parameter_list|)
block|{
name|super
operator|.
name|setInterceptStrategies
argument_list|(
name|interceptStrategies
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setLanguageResolver (LanguageResolver languageResolver)
specifier|public
name|void
name|setLanguageResolver
parameter_list|(
name|LanguageResolver
name|languageResolver
parameter_list|)
block|{
name|super
operator|.
name|setLanguageResolver
argument_list|(
name|languageResolver
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setLifecycleStrategy (LifecycleStrategy lifecycleStrategy)
specifier|public
name|void
name|setLifecycleStrategy
parameter_list|(
name|LifecycleStrategy
name|lifecycleStrategy
parameter_list|)
block|{
name|super
operator|.
name|setLifecycleStrategy
argument_list|(
name|lifecycleStrategy
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setTypeConverter (TypeConverter typeConverter)
specifier|public
name|void
name|setTypeConverter
parameter_list|(
name|TypeConverter
name|typeConverter
parameter_list|)
block|{
name|super
operator|.
name|setTypeConverter
argument_list|(
name|typeConverter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createInjector ()
specifier|protected
name|Injector
name|createInjector
parameter_list|()
block|{
return|return
operator|new
name|GuiceInjector
argument_list|(
name|injector
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|Registry
name|createRegistry
parameter_list|()
block|{
name|Context
name|context
init|=
name|createContext
argument_list|()
decl_stmt|;
return|return
operator|new
name|JndiRegistry
argument_list|(
name|context
argument_list|)
return|;
block|}
DECL|method|createContext ()
specifier|protected
name|Context
name|createContext
parameter_list|()
block|{
name|Set
argument_list|<
name|Binding
argument_list|<
name|?
argument_list|>
argument_list|>
name|bindings
init|=
name|Injectors
operator|.
name|getBindingsOf
argument_list|(
name|injector
argument_list|,
name|Context
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|bindings
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
operator|new
name|InitialContext
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|injector
operator|.
name|getInstance
argument_list|(
name|Context
operator|.
name|class
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

