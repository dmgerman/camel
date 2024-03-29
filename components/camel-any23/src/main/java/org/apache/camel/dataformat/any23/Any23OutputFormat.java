begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.any23
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|any23
package|;
end_package

begin_enum
DECL|enum|Any23OutputFormat
specifier|public
enum|enum
name|Any23OutputFormat
block|{
DECL|enumConstant|NTRIPLES
DECL|enumConstant|TURTLE
DECL|enumConstant|NQUADS
DECL|enumConstant|RDFXML
DECL|enumConstant|JSONLD
DECL|enumConstant|RDFJSON
DECL|enumConstant|RDF4JMODEL
name|NTRIPLES
block|,
name|TURTLE
block|,
name|NQUADS
block|,
name|RDFXML
block|,
name|JSONLD
block|,
name|RDFJSON
block|,
name|RDF4JMODEL
block|}
end_enum

end_unit

