begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_class
DECL|class|GoogleBigQueryConstants
specifier|public
specifier|final
class|class
name|GoogleBigQueryConstants
block|{
DECL|field|TABLE_SUFFIX
specifier|public
specifier|static
specifier|final
name|String
name|TABLE_SUFFIX
init|=
literal|"CamelGoogleBigQueryTableSuffix"
decl_stmt|;
DECL|field|TABLE_ID
specifier|public
specifier|static
specifier|final
name|String
name|TABLE_ID
init|=
literal|"CamelGoogleBigQueryTableId"
decl_stmt|;
DECL|field|INSERT_ID
specifier|public
specifier|static
specifier|final
name|String
name|INSERT_ID
init|=
literal|"CamelGoogleBigQueryInsertId"
decl_stmt|;
DECL|field|PARTITION_DECORATOR
specifier|public
specifier|static
specifier|final
name|String
name|PARTITION_DECORATOR
init|=
literal|"CamelGoogleBigQueryPartitionDecorator"
decl_stmt|;
DECL|field|TRANSLATED_QUERY
specifier|public
specifier|static
specifier|final
name|String
name|TRANSLATED_QUERY
init|=
literal|"CamelGoogleBigQueryTranslatedQuery"
decl_stmt|;
comment|/**      * Prevent instantiation.      */
DECL|method|GoogleBigQueryConstants ()
specifier|private
name|GoogleBigQueryConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

