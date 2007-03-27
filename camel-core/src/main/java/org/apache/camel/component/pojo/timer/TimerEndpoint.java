begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pojo.timer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pojo
operator|.
name|timer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|Producer
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
name|component
operator|.
name|pojo
operator|.
name|PojoExchange
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
name|DefaultEndpoint
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
name|IntrospectionSupport
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
name|URISupport
import|;
end_import

begin_comment
comment|/**  * Represents a timer endpoint that can generate periodic inbound PojoExchanges.   *  * @version $Revision: 519973 $  */
end_comment

begin_class
DECL|class|TimerEndpoint
specifier|public
class|class
name|TimerEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|PojoExchange
argument_list|>
block|{
DECL|field|component
specifier|private
specifier|final
name|TimerComponent
name|component
decl_stmt|;
DECL|field|timerName
specifier|private
specifier|final
name|String
name|timerName
decl_stmt|;
DECL|field|time
specifier|private
name|Date
name|time
decl_stmt|;
DECL|field|period
specifier|private
name|long
name|period
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|delay
specifier|private
name|long
name|delay
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|fixedRate
specifier|private
name|boolean
name|fixedRate
decl_stmt|;
DECL|field|daemon
specifier|private
name|boolean
name|daemon
init|=
literal|true
decl_stmt|;
DECL|method|TimerEndpoint (String fullURI, String timerPartURI, TimerComponent component)
specifier|public
name|TimerEndpoint
parameter_list|(
name|String
name|fullURI
parameter_list|,
name|String
name|timerPartURI
parameter_list|,
name|TimerComponent
name|component
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|fullURI
argument_list|,
name|component
operator|.
name|getContainer
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
comment|// Use a URI to extract query so they can be set as properties on the endpoint.
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|timerPartURI
argument_list|)
decl_stmt|;
name|Map
name|options
init|=
name|URISupport
operator|.
name|parseParamters
argument_list|(
name|u
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|this
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|this
operator|.
name|timerName
operator|=
name|u
operator|.
name|getPath
argument_list|()
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
argument_list|<
name|PojoExchange
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot produce to a TimerEndpoint: "
operator|+
name|getEndpointUri
argument_list|()
argument_list|)
throw|;
block|}
DECL|method|createConsumer (Processor<PojoExchange> processor)
specifier|public
name|Consumer
argument_list|<
name|PojoExchange
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
argument_list|<
name|PojoExchange
argument_list|>
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|TimerConsumer
name|consumer
init|=
operator|new
name|TimerConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
return|return
name|startService
argument_list|(
name|consumer
argument_list|)
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|PojoExchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|PojoExchange
argument_list|(
name|getContext
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getComponent ()
specifier|public
name|TimerComponent
name|getComponent
parameter_list|()
block|{
return|return
name|component
return|;
block|}
DECL|method|getTimerName ()
specifier|public
name|String
name|getTimerName
parameter_list|()
block|{
return|return
name|timerName
return|;
block|}
DECL|method|isDaemon ()
specifier|public
name|boolean
name|isDaemon
parameter_list|()
block|{
return|return
name|daemon
return|;
block|}
DECL|method|setDaemon (boolean daemon)
specifier|public
name|void
name|setDaemon
parameter_list|(
name|boolean
name|daemon
parameter_list|)
block|{
name|this
operator|.
name|daemon
operator|=
name|daemon
expr_stmt|;
block|}
DECL|method|getDelay ()
specifier|public
name|long
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
DECL|method|setDelay (long delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
block|}
DECL|method|isFixedRate ()
specifier|public
name|boolean
name|isFixedRate
parameter_list|()
block|{
return|return
name|fixedRate
return|;
block|}
DECL|method|setFixedRate (boolean fixedRate)
specifier|public
name|void
name|setFixedRate
parameter_list|(
name|boolean
name|fixedRate
parameter_list|)
block|{
name|this
operator|.
name|fixedRate
operator|=
name|fixedRate
expr_stmt|;
block|}
DECL|method|getPeriod ()
specifier|public
name|long
name|getPeriod
parameter_list|()
block|{
return|return
name|period
return|;
block|}
DECL|method|setPeriod (long period)
specifier|public
name|void
name|setPeriod
parameter_list|(
name|long
name|period
parameter_list|)
block|{
name|this
operator|.
name|period
operator|=
name|period
expr_stmt|;
block|}
DECL|method|getTime ()
specifier|public
name|Date
name|getTime
parameter_list|()
block|{
return|return
name|time
return|;
block|}
DECL|method|setTime (Date time)
specifier|public
name|void
name|setTime
parameter_list|(
name|Date
name|time
parameter_list|)
block|{
name|this
operator|.
name|time
operator|=
name|time
expr_stmt|;
block|}
block|}
end_class

end_unit

