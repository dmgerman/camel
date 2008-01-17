begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stream
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_class
DECL|class|StreamEndpoint
specifier|public
class|class
name|StreamEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|StreamExchange
argument_list|>
block|{
DECL|field|file
name|String
name|file
decl_stmt|;
DECL|field|producer
name|Producer
argument_list|<
name|StreamExchange
argument_list|>
name|producer
decl_stmt|;
DECL|field|parameters
specifier|private
name|Map
name|parameters
decl_stmt|;
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|StreamConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|StreamEndpoint (StreamComponent component, String uri, String remaining, Map parameters)
specifier|public
name|StreamEndpoint
parameter_list|(
name|StreamComponent
name|component
parameter_list|,
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|uri
operator|+
literal|" / "
operator|+
name|remaining
operator|+
literal|" / "
operator|+
name|parameters
argument_list|)
expr_stmt|;
name|this
operator|.
name|producer
operator|=
operator|new
name|StreamProducer
argument_list|(
name|this
argument_list|,
name|uri
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
DECL|method|createConsumer (Processor p)
specifier|public
name|Consumer
argument_list|<
name|StreamExchange
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
name|p
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|StreamConsumer
argument_list|(
name|this
argument_list|,
name|p
argument_list|,
name|uri
argument_list|,
name|parameters
argument_list|)
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
argument_list|<
name|StreamExchange
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|producer
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

