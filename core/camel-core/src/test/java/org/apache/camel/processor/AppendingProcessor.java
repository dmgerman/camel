begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Message
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

begin_class
DECL|class|AppendingProcessor
specifier|public
class|class
name|AppendingProcessor
implements|implements
name|Processor
block|{
DECL|field|suffixString
specifier|private
name|String
name|suffixString
decl_stmt|;
DECL|method|AppendingProcessor ()
specifier|public
name|AppendingProcessor
parameter_list|()
block|{
name|this
argument_list|(
literal|"+output"
argument_list|)
expr_stmt|;
block|}
DECL|method|AppendingProcessor (String suffix)
specifier|public
name|AppendingProcessor
parameter_list|(
name|String
name|suffix
parameter_list|)
block|{
name|suffixString
operator|=
name|suffix
expr_stmt|;
block|}
DECL|method|setSuffixString (String suffix)
specifier|public
name|void
name|setSuffixString
parameter_list|(
name|String
name|suffix
parameter_list|)
block|{
name|suffixString
operator|=
name|suffix
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// lets transform the IN message
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|body
init|=
name|in
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|body
operator|+
name|suffixString
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

