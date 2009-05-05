begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
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

begin_comment
comment|/**  * A simple class to wrap an existing processor in AOP around  * with two processors that will be executed before and after the  * main processor.  */
end_comment

begin_class
DECL|class|CxfAroundProcessor
specifier|public
class|class
name|CxfAroundProcessor
implements|implements
name|Processor
block|{
comment|// TODO: Should leverage AOP around when we have support for that in camel-core
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|before
specifier|private
specifier|final
name|Processor
name|before
decl_stmt|;
DECL|field|after
specifier|private
specifier|final
name|Processor
name|after
decl_stmt|;
DECL|method|CxfAroundProcessor (Processor processor, Processor before, Processor after)
specifier|public
name|CxfAroundProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|Processor
name|before
parameter_list|,
name|Processor
name|after
parameter_list|)
block|{
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|this
operator|.
name|before
operator|=
name|before
expr_stmt|;
name|this
operator|.
name|after
operator|=
name|after
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|before
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
try|try
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|after
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

