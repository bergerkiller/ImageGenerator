ImageGenerator
==============

ImageGenerator is a maven plugin that allows you to generate an image in target with custom text labels
Usage example:

```xml
    <!-- Version image generation -->
    <plugin>
      <groupId>com.bergerkiller</groupId>
      <artifactId>imagegenerator-maven-plugin</artifactId>
      <version>1.0</version>
      <executions>
        <execution>
        <id>Version Image Generation</id>
        <goals>
          <goal>generate_image</goal>
        </goals>
        <configuration>
          <name>BKCommonLibVersion.png</name>
          <background>https://dl.dropbox.com/u/3681706/BKCommonLibVersionSource.png</background>
          <labels>
            <label>
            <text>${project.version}</text>
            <posX>264</posX>
            <posY>162</posY>
            <color>WHITE</color>
            <font>
              <name>calibri</name>
              <bold>true</bold>
              <underline>true</underline>
              <size>22</size>
            </font>
            </label>
          </labels>
        </configuration>
        </execution>
      </executions>
    </plugin>
```