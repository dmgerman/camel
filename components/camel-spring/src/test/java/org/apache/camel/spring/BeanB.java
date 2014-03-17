begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|DisposableBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|InitializingBean
import|;
end_import

begin_class
DECL|class|BeanB
specifier|public
class|class
name|BeanB
implements|implements
name|InitializingBean
implements|,
name|DisposableBean
block|{
DECL|field|shutdownOrderBean
specifier|private
name|ShutdownOrderBean
name|shutdownOrderBean
decl_stmt|;
DECL|method|getShutdownOrderBean ()
specifier|public
name|ShutdownOrderBean
name|getShutdownOrderBean
parameter_list|()
block|{
return|return
name|shutdownOrderBean
return|;
block|}
DECL|method|setShutdownOrderBean (ShutdownOrderBean shutdownOrderBean)
specifier|public
name|void
name|setShutdownOrderBean
parameter_list|(
name|ShutdownOrderBean
name|shutdownOrderBean
parameter_list|)
block|{
name|this
operator|.
name|shutdownOrderBean
operator|=
name|shutdownOrderBean
expr_stmt|;
block|}
DECL|method|foo (String s)
specifier|public
name|String
name|foo
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|"b"
operator|+
name|s
return|;
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|shutdownOrderBean
operator|.
name|shutdown
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
name|shutdownOrderBean
operator|.
name|start
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

