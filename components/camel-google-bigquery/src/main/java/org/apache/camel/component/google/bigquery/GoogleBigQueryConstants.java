begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.bigquery
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|bigquery
package|;
end_package

begin_interface
DECL|interface|GoogleBigQueryConstants
specifier|public
interface|interface
name|GoogleBigQueryConstants
block|{
DECL|field|TABLE_SUFFIX
name|String
name|TABLE_SUFFIX
init|=
literal|"CamelGoogleBigQuery.TableSuffix"
decl_stmt|;
DECL|field|TABLE_ID
name|String
name|TABLE_ID
init|=
literal|"CamelGoogleBigQuery.TableId"
decl_stmt|;
DECL|field|INSERT_ID
name|String
name|INSERT_ID
init|=
literal|"CamelGoogleBigQuery.InsertId"
decl_stmt|;
DECL|field|PARTITION_DECORATOR
name|String
name|PARTITION_DECORATOR
init|=
literal|"CamelGoogleBigQuery.PartitionDecorator"
decl_stmt|;
block|}
end_interface

end_unit

