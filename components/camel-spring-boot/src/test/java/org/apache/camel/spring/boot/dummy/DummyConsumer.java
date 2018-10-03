begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.dummy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|dummy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Endpoint
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
name|support
operator|.
name|DefaultConsumer
import|;
end_import

begin_class
DECL|class|DummyConsumer
specifier|public
class|class
name|DummyConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|failOnRestart
specifier|private
name|boolean
name|failOnRestart
init|=
literal|true
decl_stmt|;
DECL|method|DummyConsumer (Endpoint endpoint, Processor processor)
specifier|public
name|DummyConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|DummyEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|DummyEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|isFailOnRestart ()
specifier|public
name|boolean
name|isFailOnRestart
parameter_list|()
block|{
return|return
name|failOnRestart
return|;
block|}
DECL|method|setFailOnRestart (boolean failOnRestart)
specifier|public
name|void
name|setFailOnRestart
parameter_list|(
name|boolean
name|failOnRestart
parameter_list|)
block|{
name|this
operator|.
name|failOnRestart
operator|=
name|failOnRestart
expr_stmt|;
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
name|int
name|counter
init|=
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|increment
argument_list|()
decl_stmt|;
if|if
condition|(
name|counter
operator|==
literal|2
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Forced error on restart"
argument_list|)
throw|;
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

