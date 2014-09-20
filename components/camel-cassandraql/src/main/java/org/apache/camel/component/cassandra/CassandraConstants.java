begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2014 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cassandra
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cassandra
package|;
end_package

begin_comment
comment|/**  * Cassandra QL Endpoint constants  */
end_comment

begin_class
DECL|class|CassandraConstants
specifier|public
class|class
name|CassandraConstants
block|{
comment|/**      * In Message header: CQL Query      */
DECL|field|CQL_QUERY
specifier|public
specifier|static
specifier|final
name|String
name|CQL_QUERY
init|=
literal|"CamelCqlQuery"
decl_stmt|;
block|}
end_class

end_unit

