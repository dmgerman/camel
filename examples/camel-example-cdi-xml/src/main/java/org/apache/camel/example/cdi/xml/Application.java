begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cdi.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cdi
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|ApplicationScoped
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|Destroyed
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|Initialized
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|event
operator|.
name|Observes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|Body
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
name|CamelException
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
name|Handler
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
name|cdi
operator|.
name|ImportResource
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
name|CamelEvent
operator|.
name|RouteStoppedEvent
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
name|builder
operator|.
name|Builder
operator|.
name|simple
import|;
end_import

begin_comment
comment|/**  * This example imports a Camel XML configuration file from the classpath using the  * {@code ImportResource} annotation.<p>  *  * The imported Camel XML file configures a Camel context that references CDI beans  * declared in this class.  */
end_comment

begin_class
annotation|@
name|Named
argument_list|(
literal|"matrix"
argument_list|)
annotation|@
name|ImportResource
argument_list|(
literal|"camel-context.xml"
argument_list|)
DECL|class|Application
specifier|public
class|class
name|Application
block|{
annotation|@
name|Named
annotation|@
name|Produces
DECL|field|morpheus
name|Exception
name|morpheus
init|=
operator|new
name|CamelException
argument_list|(
literal|"All I'm offering is the truth!"
argument_list|)
decl_stmt|;
annotation|@
name|Named
annotation|@
name|Produces
DECL|field|tracer
name|Processor
name|tracer
init|=
name|exchange
lambda|->
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"location"
argument_list|,
name|simple
argument_list|(
literal|"${exchangeProperty.CamelFailureRouteId}"
argument_list|)
argument_list|)
decl_stmt|;
DECL|method|login (@bserves @nitializedApplicationScoped.class) Object event)
name|void
name|login
parameter_list|(
annotation|@
name|Observes
annotation|@
name|Initialized
argument_list|(
name|ApplicationScoped
operator|.
name|class
argument_list|)
name|Object
name|event
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"ââââââââââââ  âââââââââââ    ââââ   ââââ ââââââ ââââââââââââââââ ââââââ  âââ\n"
operator|+
literal|"ââââââââââââ  âââââââââââ    âââââ âââââââââââââââââââââââââââââââââââââââââ\n"
operator|+
literal|"   âââ   ââââââââââââââ      âââââââââââââââââââ   âââ   âââââââââââ ââââââ \n"
operator|+
literal|"   âââ   ââââââââââââââ      âââââââââââââââââââ   âââ   âââââââââââ ââââââ \n"
operator|+
literal|"   âââ   âââ  âââââââââââ    âââ âââ ââââââ  âââ   âââ   âââ  ââââââââââ âââ\n"
operator|+
literal|"   âââ   âââ  âââââââââââ    âââ     ââââââ  âââ   âââ   âââ  âââââââââ  âââ"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Handler
DECL|method|terminal (@ody String body)
specifier|public
name|String
name|terminal
parameter_list|(
annotation|@
name|Body
name|String
name|body
parameter_list|)
block|{
return|return
literal|"Matrix Â» "
operator|+
name|body
return|;
block|}
DECL|method|logout (@bserves @amedR) RouteStoppedEvent event)
name|void
name|logout
parameter_list|(
annotation|@
name|Observes
annotation|@
name|Named
argument_list|(
literal|"terminal"
argument_list|)
name|RouteStoppedEvent
name|event
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"                                     __    \n"
operator|+
literal|" _____         _                   _|  |   \n"
operator|+
literal|"|  |  |___ ___| |_ _ ___ ___ ___ _| |  |   \n"
operator|+
literal|"|  |  |   | . | | | | . | . | -_| . |__|   \n"
operator|+
literal|"|_____|_|_|  _|_|___|_  |_  |___|___|__|   \n"
operator|+
literal|"          |_|       |___|___|              "
argument_list|)
expr_stmt|;
block|}
DECL|method|shutdown (@bserves @estroyedApplicationScoped.class) Object event)
name|void
name|shutdown
parameter_list|(
annotation|@
name|Observes
annotation|@
name|Destroyed
argument_list|(
name|ApplicationScoped
operator|.
name|class
argument_list|)
name|Object
name|event
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|" _____ _       _     _                     \n"
operator|+
literal|"|   __| |_ _ _| |_ _| |___ _ _ _ ___       \n"
operator|+
literal|"|__   |   | | |  _| . | . | | | |   |_ _ _ \n"
operator|+
literal|"|_____|_|_|___|_| |___|___|_____|_|_|_|_|_|"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

