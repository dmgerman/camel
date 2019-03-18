begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
package|;
end_package

begin_interface
DECL|interface|MDNField
specifier|public
interface|interface
name|MDNField
block|{
comment|/**      * Field Name for Reporting UA      */
DECL|field|REPORTING_UA
specifier|public
specifier|static
specifier|final
name|String
name|REPORTING_UA
init|=
literal|"Reporting-UA"
decl_stmt|;
comment|/**      * Field Name for MDN Gateway      */
DECL|field|MDN_GATEWAY
specifier|public
specifier|static
specifier|final
name|String
name|MDN_GATEWAY
init|=
literal|"MDN-Gateway"
decl_stmt|;
comment|/**      * Field Name for Final Recipient      */
DECL|field|FINAL_RECIPIENT
specifier|public
specifier|static
specifier|final
name|String
name|FINAL_RECIPIENT
init|=
literal|"Final-Recipient"
decl_stmt|;
comment|/**      * Field Name for Original Message IDX      */
DECL|field|ORIGINAL_MESSAGE_ID
specifier|public
specifier|static
specifier|final
name|String
name|ORIGINAL_MESSAGE_ID
init|=
literal|"Original-Message-ID"
decl_stmt|;
comment|/**      * Field Name for Disposition      */
DECL|field|DISPOSITION
specifier|public
specifier|static
specifier|final
name|String
name|DISPOSITION
init|=
literal|"Disposition"
decl_stmt|;
comment|/**      * Field Name for Failure      */
DECL|field|FAILURE
specifier|public
specifier|static
specifier|final
name|String
name|FAILURE
init|=
literal|"Failure"
decl_stmt|;
comment|/**      * Field Name for Error      */
DECL|field|ERROR
specifier|public
specifier|static
specifier|final
name|String
name|ERROR
init|=
literal|"Error"
decl_stmt|;
comment|/**      * Field Name for Warning      */
DECL|field|WARNING
specifier|public
specifier|static
specifier|final
name|String
name|WARNING
init|=
literal|"Warning"
decl_stmt|;
comment|/**      * Field Name for Received Content MIC      */
DECL|field|RECEIVED_CONTENT_MIC
specifier|public
specifier|static
specifier|final
name|String
name|RECEIVED_CONTENT_MIC
init|=
literal|"Received-content-MIC"
decl_stmt|;
block|}
end_interface

end_unit

