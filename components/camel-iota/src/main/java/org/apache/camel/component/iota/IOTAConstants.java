begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.iota
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|iota
package|;
end_package

begin_import
import|import
name|jota
operator|.
name|utils
operator|.
name|Constants
import|;
end_import

begin_class
DECL|class|IOTAConstants
specifier|public
specifier|final
class|class
name|IOTAConstants
block|{
DECL|field|SEED_HEADER
specifier|public
specifier|static
specifier|final
name|String
name|SEED_HEADER
init|=
literal|"CamelIOTASeed"
decl_stmt|;
DECL|field|VALUE_HEADER
specifier|public
specifier|static
specifier|final
name|String
name|VALUE_HEADER
init|=
literal|"CamelIOTAValue"
decl_stmt|;
DECL|field|TO_ADDRESS_HEADER
specifier|public
specifier|static
specifier|final
name|String
name|TO_ADDRESS_HEADER
init|=
literal|"CamelIOTAToAddress"
decl_stmt|;
DECL|field|ADDRESS_INDEX_HEADER
specifier|public
specifier|static
specifier|final
name|String
name|ADDRESS_INDEX_HEADER
init|=
literal|"CamelIOTAAddressIndex"
decl_stmt|;
DECL|field|ADDRESS_START_INDEX_HEADER
specifier|public
specifier|static
specifier|final
name|String
name|ADDRESS_START_INDEX_HEADER
init|=
literal|"CamelIOTAAddressStartIndex"
decl_stmt|;
DECL|field|ADDRESS_END_INDEX_HEADER
specifier|public
specifier|static
specifier|final
name|String
name|ADDRESS_END_INDEX_HEADER
init|=
literal|"CamelIOTAAddressEndIndex"
decl_stmt|;
DECL|field|MIN_WEIGHT_MAGNITUDE
specifier|protected
specifier|static
specifier|final
name|int
name|MIN_WEIGHT_MAGNITUDE
init|=
literal|14
decl_stmt|;
DECL|field|DEPTH
specifier|protected
specifier|static
specifier|final
name|int
name|DEPTH
init|=
literal|9
decl_stmt|;
DECL|field|TAG_LENGTH
specifier|protected
specifier|static
specifier|final
name|int
name|TAG_LENGTH
init|=
name|Constants
operator|.
name|TAG_LENGTH
decl_stmt|;
DECL|field|MESSAGE_LENGTH
specifier|protected
specifier|static
specifier|final
name|int
name|MESSAGE_LENGTH
init|=
name|Constants
operator|.
name|MESSAGE_LENGTH
decl_stmt|;
DECL|field|SEND_TRANSFER_OPERATION
specifier|protected
specifier|static
specifier|final
name|String
name|SEND_TRANSFER_OPERATION
init|=
literal|"sendTransfer"
decl_stmt|;
DECL|field|GET_NEW_ADDRESS_OPERATION
specifier|protected
specifier|static
specifier|final
name|String
name|GET_NEW_ADDRESS_OPERATION
init|=
literal|"getNewAddress"
decl_stmt|;
DECL|field|GET_TRANSFERS_OPERATION
specifier|protected
specifier|static
specifier|final
name|String
name|GET_TRANSFERS_OPERATION
init|=
literal|"getTransfers"
decl_stmt|;
DECL|method|IOTAConstants ()
specifier|private
name|IOTAConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

