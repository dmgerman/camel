begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|util
operator|.
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * A processor which ensures wrapping processors is having lifecycle handled.  *  * @version   */
end_comment

begin_class
DECL|class|WrapProcessor
specifier|public
class|class
name|WrapProcessor
extends|extends
name|DelegateAsyncProcessor
block|{
DECL|field|wrapped
specifier|private
specifier|final
name|Processor
name|wrapped
decl_stmt|;
DECL|method|WrapProcessor (Processor processor, Processor wrapped)
specifier|public
name|WrapProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|Processor
name|wrapped
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|wrapped
operator|=
name|wrapped
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
literal|"Wrap["
operator|+
name|wrapped
operator|+
literal|"] -> "
operator|+
name|processor
return|;
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
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|wrapped
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|wrapped
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

