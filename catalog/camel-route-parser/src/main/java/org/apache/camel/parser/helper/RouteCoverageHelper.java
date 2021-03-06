begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.parser.helper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
operator|.
name|helper
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
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
name|catalog
operator|.
name|CamelCatalog
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
name|catalog
operator|.
name|DefaultCamelCatalog
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
name|parser
operator|.
name|model
operator|.
name|CoverageData
import|;
end_import

begin_comment
comment|/**  * Helper to provide route coverage details.  */
end_comment

begin_class
DECL|class|RouteCoverageHelper
specifier|public
specifier|final
class|class
name|RouteCoverageHelper
block|{
DECL|method|RouteCoverageHelper ()
specifier|private
name|RouteCoverageHelper
parameter_list|()
block|{     }
comment|/**      * Parses the dumped route coverage data and creates a line by line coverage data      *      * @param directory  the directory with the dumped route coverage data      * @param routeId    the route id to gather, must not be null.      * @return line by line coverage data      */
DECL|method|parseDumpRouteCoverageByRouteId (String directory, String routeId)
specifier|public
specifier|static
name|List
argument_list|<
name|CoverageData
argument_list|>
name|parseDumpRouteCoverageByRouteId
parameter_list|(
name|String
name|directory
parameter_list|,
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|CoverageData
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|routeId
operator|==
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
name|File
index|[]
name|files
init|=
operator|new
name|File
argument_list|(
name|directory
argument_list|)
operator|.
name|listFiles
argument_list|(
name|f
lambda|->
name|f
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".xml"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|files
operator|==
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
name|CamelCatalog
name|catalog
init|=
operator|new
name|DefaultCamelCatalog
argument_list|(
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
try|try
init|(
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
init|)
block|{
name|Document
name|dom
init|=
name|XmlLineNumberParser
operator|.
name|parseXml
argument_list|(
name|fis
argument_list|)
decl_stmt|;
name|NodeList
name|routes
init|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"route"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|routes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|route
init|=
name|routes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|id
init|=
name|route
operator|.
name|getAttributes
argument_list|()
operator|.
name|getNamedItem
argument_list|(
literal|"id"
argument_list|)
operator|.
name|getNodeValue
argument_list|()
decl_stmt|;
name|String
name|customId
init|=
name|route
operator|.
name|getAttributes
argument_list|()
operator|.
name|getNamedItem
argument_list|(
literal|"customId"
argument_list|)
operator|!=
literal|null
condition|?
name|route
operator|.
name|getAttributes
argument_list|()
operator|.
name|getNamedItem
argument_list|(
literal|"customId"
argument_list|)
operator|.
name|getNodeValue
argument_list|()
else|:
literal|"false"
decl_stmt|;
comment|// must be the target route and the route must be explicit assigned with that route id (not anonymous route)
if|if
condition|(
literal|"true"
operator|.
name|equals
argument_list|(
name|customId
argument_list|)
operator|&&
name|routeId
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
comment|// parse each route and build a List<CoverageData> for line by line coverage data
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|parseRouteData
argument_list|(
name|catalog
argument_list|,
name|route
argument_list|,
name|answer
argument_list|,
name|counter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|parseDumpRouteCoverageByClassAndTestMethod (String directory)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|CoverageData
argument_list|>
argument_list|>
name|parseDumpRouteCoverageByClassAndTestMethod
parameter_list|(
name|String
name|directory
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|CoverageData
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|File
index|[]
name|files
init|=
operator|new
name|File
argument_list|(
name|directory
argument_list|)
operator|.
name|listFiles
argument_list|(
name|f
lambda|->
name|f
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".xml"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|files
operator|==
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
name|CamelCatalog
name|catalog
init|=
operator|new
name|DefaultCamelCatalog
argument_list|(
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
try|try
init|(
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
init|)
block|{
name|Document
name|dom
init|=
name|XmlLineNumberParser
operator|.
name|parseXml
argument_list|(
name|fis
argument_list|)
decl_stmt|;
name|NodeList
name|routes
init|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"route"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|routes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|route
init|=
name|routes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// parse each route and build a List<CoverageData> for line by line coverage data
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|CoverageData
argument_list|>
name|data
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|parseRouteData
argument_list|(
name|catalog
argument_list|,
name|route
argument_list|,
name|data
argument_list|,
name|counter
argument_list|)
expr_stmt|;
comment|// create a key which is based on the file name without extension
name|String
name|key
init|=
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// strip .xml extension
name|key
operator|=
name|key
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|key
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
comment|// is there existing data
name|List
argument_list|<
name|CoverageData
argument_list|>
name|existing
init|=
name|answer
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
operator|!=
literal|null
condition|)
block|{
name|existing
operator|.
name|addAll
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|parseRouteData (CamelCatalog catalog, Node node, List<CoverageData> data, AtomicInteger counter)
specifier|private
specifier|static
name|void
name|parseRouteData
parameter_list|(
name|CamelCatalog
name|catalog
parameter_list|,
name|Node
name|node
parameter_list|,
name|List
argument_list|<
name|CoverageData
argument_list|>
name|data
parameter_list|,
name|AtomicInteger
name|counter
parameter_list|)
block|{
comment|// must be a known EIP model
name|String
name|key
init|=
name|node
operator|.
name|getNodeName
argument_list|()
decl_stmt|;
name|boolean
name|valid
init|=
name|catalog
operator|.
name|findModelNames
argument_list|()
operator|.
name|contains
argument_list|(
name|key
argument_list|)
decl_stmt|;
comment|// skip route as we use from instead
if|if
condition|(
operator|!
name|valid
condition|)
block|{
return|return;
block|}
comment|// inlined error handler, on completion etc should be skipped (and currently not supported in route coverage)
name|boolean
name|skip
init|=
literal|"onException"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
literal|"onCompletion"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
literal|"intercept"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
literal|"interceptFrom"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
literal|"interceptSendToEndpoint"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
comment|// only calculate for elements within the route or children of policy/transaction
if|if
condition|(
operator|!
literal|"route"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|&&
operator|!
literal|"policy"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|&&
operator|!
literal|"transacted"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
name|Node
name|total
init|=
name|node
operator|.
name|getAttributes
argument_list|()
operator|.
name|getNamedItem
argument_list|(
literal|"exchangesTotal"
argument_list|)
decl_stmt|;
if|if
condition|(
name|total
operator|!=
literal|null
condition|)
block|{
name|count
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|total
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|CoverageData
name|holder
init|=
name|data
operator|.
name|size
argument_list|()
operator|>
name|counter
operator|.
name|get
argument_list|()
condition|?
name|data
operator|.
name|get
argument_list|(
name|counter
operator|.
name|get
argument_list|()
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|holder
operator|!=
literal|null
operator|&&
name|holder
operator|.
name|getNode
argument_list|()
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|count
operator|+=
name|holder
operator|.
name|getCount
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|holder
operator|==
literal|null
condition|)
block|{
comment|// add new
name|data
operator|.
name|add
argument_list|(
name|counter
operator|.
name|get
argument_list|()
argument_list|,
operator|new
name|CoverageData
argument_list|(
name|key
argument_list|,
name|count
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// replace existing
name|data
operator|.
name|set
argument_list|(
name|counter
operator|.
name|get
argument_list|()
argument_list|,
operator|new
name|CoverageData
argument_list|(
name|key
argument_list|,
name|count
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// advance counter
name|counter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
comment|// any children
name|NodeList
name|children
init|=
name|node
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
if|if
condition|(
name|children
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|children
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|children
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|instanceof
name|Element
condition|)
block|{
name|parseRouteData
argument_list|(
name|catalog
argument_list|,
name|child
argument_list|,
name|data
argument_list|,
name|counter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

